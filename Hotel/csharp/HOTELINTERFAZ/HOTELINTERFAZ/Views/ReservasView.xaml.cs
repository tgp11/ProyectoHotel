using HOTELINTERFAZ.Ventanas;
using HOTELINTERFAZ.ViewModels;
using System.Windows;
using System.Windows.Controls;

namespace HOTELINTERFAZ.Views
{
    public partial class ReservasView : UserControl
    {
        private ReservasViewModel _reservasVM = new ReservasViewModel();
        private HabitacionesViewModel _habitacionesVM = new HabitacionesViewModel();

        public ReservasView()
        {
            InitializeComponent();
            DataContext = _reservasVM;
        }

        private void NuevaReserva_Click(object sender, RoutedEventArgs e)
        {
            var nuevaReservaWindow = new NuevaReservaWindow(_reservasVM, _habitacionesVM);
            nuevaReservaWindow.ShowDialog();
        }

        private async void Cancelar_Click(object sender, RoutedEventArgs e)
        {
            // ❌ No hay selección → abrir ventana de reservas canceladas
            if (_reservasVM.ReservaSeleccionada == null)
            {
                var ventanaCancelar = new BuscarReservasParaCancelarWindow(_reservasVM);
                ventanaCancelar.ShowDialog();
                return;
            }

            // ✅ Hay selección
            if (_reservasVM.ReservaSeleccionada.Cancelacion)
            {
                MessageBox.Show("La reserva ya está cancelada.");
                return;
            }

            // Confirmar cancelación
            var confirm = MessageBox.Show(
                "¿Desea cancelar esta reserva?",
                "Confirmar cancelación",
                MessageBoxButton.YesNo,
                MessageBoxImage.Warning
            );

            if (confirm != MessageBoxResult.Yes)
                return;

            // Cancelar la reserva usando ViewModel
            bool exito = await _reservasVM.CancelarReservaAsync(_reservasVM.ReservaSeleccionada.Id);

            if (exito)
            {
                MessageBox.Show("Reserva cancelada correctamente.");
            }
            else
            {
                MessageBox.Show("Error al cancelar la reserva.");
            }
        }



        private async void Eliminar_Click(object sender, RoutedEventArgs e)
        {
            if (_reservasVM.ReservaSeleccionada == null)
            {
                // ❌ No hay selección → abrir ventana de búsqueda de canceladas
                var buscarReservasCanceladasWindow = new BuscarReservasCanceladasWindow(_reservasVM);
                buscarReservasCanceladasWindow.ShowDialog();
                return;
            }

            if (!_reservasVM.ReservaSeleccionada.Cancelacion)
            {
                MessageBox.Show("Solo se pueden eliminar reservas canceladas.");
                return;
            }

            var confirm = MessageBox.Show(
                "¿Eliminar definitivamente la reserva?",
                "Confirmar",
                MessageBoxButton.YesNo
            );

            if (confirm == MessageBoxResult.Yes)
            {
                await _reservasVM.EliminarReserva(_reservasVM.ReservaSeleccionada.Id);
            }
        }

    }
}
