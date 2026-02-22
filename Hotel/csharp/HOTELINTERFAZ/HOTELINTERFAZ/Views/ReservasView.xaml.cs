using HOTELINTERFAZ.Ventanas;
using HOTELINTERFAZ.ViewModels;
using System.Windows;
using System.Windows.Controls;

namespace HOTELINTERFAZ.Views
{
    public partial class ReservasView : UserControl
    {
        private HabitacionesViewModel _habitacionesVM;
        private ReservasViewModel _reservasVM;
        private ClientesViewModel _clientesVM;

        public ReservasView()
        {
            InitializeComponent();

            _habitacionesVM = new HabitacionesViewModel();
            _clientesVM = new ClientesViewModel();
            _reservasVM = new ReservasViewModel();

            DataContext = _reservasVM;
        }

        private void NuevaReserva_Click(object sender, RoutedEventArgs e)
        {
            var ventana = new NuevaReservaWindow(
                _reservasVM,
                _habitacionesVM,
                _clientesVM);

            ventana.ShowDialog();
        }

        private async void Cancelar_Click(object sender, RoutedEventArgs e)
        {
            if (_reservasVM.ReservaSeleccionada == null)
            {
                MessageBox.Show("Seleccione una reserva.");
                return;
            }

            bool exito = await _reservasVM
                .CancelarReservaAsync(_reservasVM.ReservaSeleccionada.Id);

            MessageBox.Show(exito
                ? "Reserva cancelada."
                : "Error al cancelar.");
        }

        private async void Eliminar_Click(object sender, RoutedEventArgs e)
        {
            if (_reservasVM.ReservaSeleccionada == null)
            {
                MessageBox.Show("Seleccione una reserva.");
                return;
            }

            if (!_reservasVM.ReservaSeleccionada.Cancelacion)
            {
                MessageBox.Show("Solo se pueden eliminar reservas canceladas.");
                return;
            }

            bool exito = await _reservasVM
                .EliminarReservaAsync(_reservasVM.ReservaSeleccionada.Id);

            MessageBox.Show(exito
                ? "Reserva eliminada."
                : "Error al eliminar.");
        }
    }
}