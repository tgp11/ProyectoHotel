using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Shapes;
using HOTELINTERFAZ.Models;
using HOTELINTERFAZ.ViewModels;

namespace HOTELINTERFAZ.Views
{
    /// <summary>
    /// Lógica de interacción para HabitacionesView.xaml
    /// </summary>
    public partial class HabitacionesView : UserControl
    {
        public ObservableCollection<string> TiposHabitacion { get; } =
            new() { "individual", "doble", "suite" };
        private readonly HabitacionesViewModel _vm = new();
        // Alias para no reescribir todo el código antiguo
        private ObservableCollection<Habitacion> _habitaciones => _vm.Habitaciones;
        private readonly ICollectionView _view;
        public HabitacionesView()
        {
            InitializeComponent();
            DgHabitaciones.ItemsSource = _habitaciones;
            _view = CollectionViewSource.GetDefaultView(DgHabitaciones.ItemsSource);
            _view.Filter = FilterHabitaciones;
        }
        private bool FilterHabitaciones(object obj)
        {
            if (obj is not Habitacion h) return false;
            var q = TxtBuscar?.Text?.Trim().ToLower() ?? "";
            if (string.IsNullOrWhiteSpace(q)) return true;
            return h.Numero.ToString().Contains(q)
                   || (h.Tipo?.ToLower().Contains(q) ?? false);
        }
        private void Editar_Click(object sender, RoutedEventArgs e)
        {
            if (DgHabitaciones.SelectedItem is not Habitacion selected)
            {
                MessageBox.Show("Selecciona una habitación para editar.");
                return;
            }
            DgHabitaciones.CurrentCell =
                new DataGridCellInfo(selected, DgHabitaciones.Columns[0]);
            DgHabitaciones.BeginEdit();
        }
        private async void Eliminar_Click(object sender, RoutedEventArgs e)
        {
            if (DgHabitaciones.SelectedItem is not Habitacion selected)
            {
                MessageBox.Show("Selecciona una habitación para eliminar.");
                return;
            }
            var res = MessageBox.Show(
                $"¿Eliminar la habitación {selected.Numero}?",
                "Confirmación",
                MessageBoxButton.YesNo,
                MessageBoxImage.Warning);
            if (res != MessageBoxResult.Yes) return;
            if (string.IsNullOrWhiteSpace(selected.Id))
            {
                _habitaciones.Remove(selected);
                _view.Refresh();
                return;
            }
            var ok = await _vm.EliminarHabitacionAsync(selected);
            if (ok)
            {
                _habitaciones.Remove(selected);
                _view.Refresh();
            }
        }
        private void Buscar_Click(object sender, RoutedEventArgs e)
        {
            _view.Refresh();
        }
        private async void DgHabitaciones_RowEditEnding(object sender, DataGridRowEditEndingEventArgs e)
        {
            if (e.EditAction != DataGridEditAction.Commit) return;
            if (e.Row.Item is not Habitacion h) return;
            await Dispatcher.InvokeAsync(() => { });
            // VALIDACIONES
            if (h.Numero <= 0 ||
                string.IsNullOrWhiteSpace(h.Tipo) ||
                h.MaxOcupantes <= 0 ||
                h.PrecioNoche < 0)
            {
                MessageBox.Show("Datos no válidos.");
                DgHabitaciones.CancelEdit(DataGridEditingUnit.Row);
                return;
            }
            int repes = _habitaciones.Count(x => x.Numero == h.Numero);
            if (repes > 1)
            {
                MessageBox.Show("Ya existe una habitación con ese número.");
                DgHabitaciones.CancelEdit(DataGridEditingUnit.Row);
                return;
            }
            // API
            if (string.IsNullOrWhiteSpace(h.Id))
            {
                // POST
                var created = await _vm.CrearHabitacionAsync(h);
                if (created == null) return;
                h.Id = created.Id;
            }
            else
            {
                // PUT
                await _vm.ActualizarHabitacionAsync(h);
            }
            _view.Refresh();
        }

        private void Nueva_Click(object sender, RoutedEventArgs e)
        {
            var nueva = new Habitacion
            {
                // Id vacío → se hará POST al terminar la edición
                Id = null,
                Numero = GetNextNumeroDisponible(),
                Tipo = "individual",
                Descripcion = "",
                MaxOcupantes = 1,
                PrecioNoche = 0,
                Disponible = true
            };

            _habitaciones.Add(nueva);

            DgHabitaciones.SelectedItem = nueva;
            DgHabitaciones.ScrollIntoView(nueva);

            DgHabitaciones.Dispatcher.InvokeAsync(() =>
            {
                DgHabitaciones.CurrentCell =
                    new DataGridCellInfo(nueva, DgHabitaciones.Columns[0]);
                DgHabitaciones.BeginEdit();
            });

        }
        private int GetNextNumeroDisponible()
        {
            if (_habitaciones.Count == 0) return 1;
            int max = _habitaciones.Max(h => h.Numero);
            return max + 1;
        }
    }
}

