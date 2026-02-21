using System;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Input;
using HOTELINTERFAZ.Models;
using HOTELINTERFAZ.ViewModels;

namespace HOTELINTERFAZ.Views
{
    public partial class HabitacionesView : UserControl
    {
        private readonly HabitacionesViewModel _vm;
        private readonly ICollectionView _view;

        public HabitacionesView(HabitacionesViewModel vm)
        {
            InitializeComponent();

            _vm = vm;
            DataContext = _vm;

            DgHabitaciones.ItemsSource = _vm.Habitaciones;

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

        // ===== BOTONES =====

        private void Nueva_Click(object sender, RoutedEventArgs e)
        {
            var nueva = new Habitacion
            {
                Numero = GetNextNumeroDisponible(),
                Tipo = "",
                MaxOcupantes = 1,
                PrecioNoche = 0,
                Disponible = true
            };

            _vm.Habitaciones.Add(nueva);

            DgHabitaciones.SelectedItem = nueva;
            DgHabitaciones.ScrollIntoView(nueva);

            DgHabitaciones.Dispatcher.InvokeAsync(() =>
            {
                DgHabitaciones.CurrentCell = new DataGridCellInfo(nueva, DgHabitaciones.Columns[0]);
                DgHabitaciones.BeginEdit();
                Keyboard.Focus(DgHabitaciones);
            });
        }

        private void Editar_Click(object sender, RoutedEventArgs e)
        {
            if (DgHabitaciones.SelectedItem is not Habitacion selected)
            {
                MessageBox.Show("Selecciona una habitación para editar.", "Info",
                    MessageBoxButton.OK, MessageBoxImage.Information);
                return;
            }

            DgHabitaciones.ScrollIntoView(selected);
            DgHabitaciones.CurrentCell = new DataGridCellInfo(selected, DgHabitaciones.Columns[0]);
            DgHabitaciones.BeginEdit();
        }

        private async void Eliminar_Click(object sender, RoutedEventArgs e)
        {
            if (DgHabitaciones.SelectedItem is not Habitacion selected)
            {
                MessageBox.Show("Selecciona una habitación para eliminar.", "Info",
                    MessageBoxButton.OK, MessageBoxImage.Information);
                return;
            }

            var res = MessageBox.Show(
                $"¿Eliminar la habitación {selected.Numero}?",
                "Confirmación",
                MessageBoxButton.YesNo,
                MessageBoxImage.Warning);

            if (res == MessageBoxResult.Yes)
            {
                _vm.Habitaciones.Remove(selected);
                _view.Refresh();
            }
        }

        private void Buscar_Click(object sender, RoutedEventArgs e)
        {
            _view.Refresh();
        }

        // ===== VALIDACIÓN =====

        private void DgHabitaciones_RowEditEnding(object sender, DataGridRowEditEndingEventArgs e)
        {
            if (e.EditAction != DataGridEditAction.Commit) return;
            if (e.Row.Item is not Habitacion h) return;

            // Muy importante: lanzar el guardado DESPUÉS de que el DataGrid termine el commit
            Dispatcher.BeginInvoke(new Action(async () =>
            {
                try
                {
                    // ===== VALIDACIÓN (la tuya, igual) =====
                    if (h.Numero <= 0) { ShowValidation("El número debe ser mayor que 0.", e); return; }
                    if (string.IsNullOrWhiteSpace(h.Tipo)) { ShowValidation("El tipo es obligatorio.", e); return; }
                    if (h.MaxOcupantes <= 0) { ShowValidation("La capacidad debe ser mayor que 0.", e); return; }
                    if (h.PrecioNoche < 0) { ShowValidation("El precio/noche no puede ser negativo.", e); return; }

                    int repes = _vm.Habitaciones.Count(x => x.Numero == h.Numero);
                    if (repes > 1) { ShowValidation("Ya existe una habitación con ese número.", e); return; }

                    // ===== GUARDADO API =====
                    if (string.IsNullOrWhiteSpace(h.Id))
                    {
                        // CREATE (POST)
                        var creada = await _vm.CrearHabitacionAsync(h);

                        // Si la API devuelve el _id, lo metemos en el objeto que está en la lista
                        if (creada != null && !string.IsNullOrWhiteSpace(creada.Id))
                            h.Id = creada.Id;
                    }
                    else
                    {
                        // UPDATE (PUT)
                        await _vm.ActualizarHabitacionAsync(h);
                    }

                    _view.Refresh();
                }
                catch (Exception ex)
                {
                    MessageBox.Show(ex.Message, "Error guardando", MessageBoxButton.OK, MessageBoxImage.Error);
                }

            }), System.Windows.Threading.DispatcherPriority.Background);
        }

        private void ShowValidation(string mensaje, DataGridRowEditEndingEventArgs e)
        {
            MessageBox.Show(mensaje, "Validación",
                MessageBoxButton.OK, MessageBoxImage.Warning);

            DgHabitaciones.CancelEdit(DataGridEditingUnit.Row);

            DgHabitaciones.Dispatcher.InvokeAsync(() =>
            {
                DgHabitaciones.SelectedItem = e.Row.Item;
                DgHabitaciones.ScrollIntoView(e.Row.Item);
                DgHabitaciones.BeginEdit();
            });
        }

        private int GetNextNumeroDisponible()
        {
            if (_vm.Habitaciones.Count == 0) return 1;
            return _vm.Habitaciones.Max(h => h.Numero) + 1;
        }
    }
}
