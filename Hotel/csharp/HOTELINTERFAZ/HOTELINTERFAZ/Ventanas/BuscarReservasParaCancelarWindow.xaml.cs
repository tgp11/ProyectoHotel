using HOTELINTERFAZ.Models;
using HOTELINTERFAZ.ViewModels;
using System.Collections.ObjectModel;
using System.Linq;
using System.Windows;
using System.Windows.Controls;

namespace HOTELINTERFAZ.Ventanas
{
    public partial class BuscarReservasParaCancelarWindow : Window
    {
        private readonly ReservasViewModel _vm;
        private ObservableCollection<Reserva> _reservasFiltradas = new();

        public BuscarReservasParaCancelarWindow(ReservasViewModel vm)
        {
            InitializeComponent();
            _vm = vm;
            dgReservas.ItemsSource = _reservasFiltradas;
        }

        private async void Buscar_Click2(object sender, RoutedEventArgs e)
        {
            string dni = txtDni.Text.Trim();

            if (string.IsNullOrEmpty(dni))
            {
                MessageBox.Show("Ingresa un DNI.");
                return;
            }

            // 1️⃣ Obtener todas las reservas del VM
            var todasReservas = _vm.Reservas.ToList();

            // 2️⃣ Obtener las canceladas
            var canceladas = todasReservas.Where(r => r.Cancelacion).ToList();

            // 3️⃣ Crear la diferencia: activas = todas - canceladas
            var activas = todasReservas
                .Where(r => !canceladas.Any(c => c.Id == r.Id))
                .Where(r => r.Cliente != null && r.Cliente.Dni == dni)
                .ToList();

            _reservasFiltradas.Clear();
            foreach (var r in activas)
                _reservasFiltradas.Add(r);

            if (!_reservasFiltradas.Any())
                MessageBox.Show("No hay reservas activas para ese DNI.");
        }

        private async void Cancelar_Click(object sender, RoutedEventArgs e)
        {
            if (sender is Button btn && btn.DataContext is Reserva reserva)
            {
                var confirm = MessageBox.Show(
                    $"¿Desea cancelar la reserva de {reserva.Cliente.Nombre} (DNI: {reserva.Cliente.Dni})?",
                    "Confirmar cancelación",
                    MessageBoxButton.YesNo,
                    MessageBoxImage.Warning
                );

                if (confirm != MessageBoxResult.Yes)
                    return;

                bool exito = await _vm.CancelarReservaAsync(reserva.Id);

                if (exito)
                {
                    MessageBox.Show("Reserva cancelada correctamente.");
                    _reservasFiltradas.Remove(reserva); // Actualiza DataGrid
                }
                else
                {
                    MessageBox.Show("Error al cancelar la reserva.");
                }
            }
        }
    }
}
