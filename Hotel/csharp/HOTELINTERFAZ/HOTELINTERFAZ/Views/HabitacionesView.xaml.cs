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
using InterfazInterna.Models;

namespace HOTELINTERFAZ.Views
{
    /// <summary>
    /// Lógica de interacción para HabitacionesView.xaml
    /// </summary>
    public partial class HabitacionesView : UserControl
    private readonly ICollectionView _view;
    {
        public HabitacionesView()
        {
            InitializeComponent();

            // Datos de ejemplo (modo local)
            _habitaciones.Add(new Habitacion
            {
                Id = "696a5c6621c934c10e410874",
                Numero = 102,
                Tipo = "individual",
                PrecioNoche = 70,
                MaxOcupantes = 1,
                Disponible = true
            });

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

        // ===== BOTONES =====

        private void Nueva_Click(object sender, RoutedEventArgs e)
        {
            // Crea una fila nueva con valores por defecto
            var nueva = new Habitacion
            {
                Numero = GetNextNumeroDisponible(),
                Tipo = "",
                MaxOcupantes = 1,
                PrecioNoche = 0,
                Disponible = true
            };

            _habitaciones.Add(nueva);

            // Seleccionarla y entrar en edición
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

            // Entrar en edición de la fila seleccionada
            DgHabitaciones.ScrollIntoView(selected);
            DgHabitaciones.CurrentCell = new DataGridCellInfo(selected, DgHabitaciones.Columns[0]);
            DgHabitaciones.BeginEdit();
        }

        private void Eliminar_Click(object sender, RoutedEventArgs e)
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
                _habitaciones.Remove(selected);
                _view.Refresh();
            }
        }

        private void Buscar_Click(object sender, RoutedEventArgs e)
        {
            _view.Refresh();
        }

        // ===== VALIDACIÓN AL TERMINAR EDICIÓN DE FILA =====

        private void DgHabitaciones_RowEditEnding(object sender, DataGridRowEditEndingEventArgs e)
        {
            if (e.EditAction != DataGridEditAction.Commit) return;
            if (e.Row.Item is not Habitacion h) return;

            // Forzar commit para tener valores finales
            Dispatcher.InvokeAsync(() =>
            {
                // Validaciones básicas
                if (h.Numero <= 0)
                {
                    MessageBox.Show("El número debe ser mayor que 0.", "Validación",
                        MessageBoxButton.OK, MessageBoxImage.Warning);
                    CancelEditKeepRow(e);
                    return;
                }

                if (string.IsNullOrWhiteSpace(h.Tipo))
                {
                    MessageBox.Show("El tipo es obligatorio.", "Validación",
                        MessageBoxButton.OK, MessageBoxImage.Warning);
                    CancelEditKeepRow(e);
                    return;
                }

                if (h.MaxOcupantes <= 0)
                {
                    MessageBox.Show("La capacidad debe ser mayor que 0.", "Validación",
                        MessageBoxButton.OK, MessageBoxImage.Warning);
                    CancelEditKeepRow(e);
                    return;
                }

                if (h.PrecioNoche < 0)
                {
                    MessageBox.Show("El precio/noche no puede ser negativo.", "Validación",
                        MessageBoxButton.OK, MessageBoxImage.Warning);
                    CancelEditKeepRow(e);
                    return;
                }

                // No duplicar Numero
                int repes = _habitaciones.Count(x => x.Numero == h.Numero);
                if (repes > 1)
                {
                    MessageBox.Show("Ya existe una habitación con ese número.", "Validación",
                        MessageBoxButton.OK, MessageBoxImage.Warning);
                    CancelEditKeepRow(e);
                    return;
                }

                _view.Refresh();
            });
        }

        private void CancelEditKeepRow(DataGridRowEditEndingEventArgs e)
        {
            // Cancela la edición y vuelve a editar esa misma fila para que lo corrijan
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
            if (_habitaciones.Count == 0) return 1;
            int max = _habitaciones.Max(h => h.Numero);
            return max + 1;
        }
    }
}

