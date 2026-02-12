using HOTELINTERFAZ.Models;
using HOTELINTERFAZ.ViewModels;
using System;
using System.Collections.ObjectModel;
using System.Linq;
using System.Windows;
using System.Windows.Controls;

namespace HOTELINTERFAZ.Ventanas
{
    public partial class NuevaReservaWindow : Window
    {
        private readonly ReservasViewModel _reservasVM;
        private readonly HabitacionesViewModel _habitacionesVM;
        private readonly ClientesViewModel _clientesVM;

        public ObservableCollection<Habitacion> HabitacionesDisponibles { get; set; } = new();

        public NuevaReservaWindow(ReservasViewModel reservasVM, HabitacionesViewModel habitacionesVM, ClientesViewModel clientesVM)
        {
            InitializeComponent();
            _reservasVM = reservasVM;
            _habitacionesVM = habitacionesVM;
            _clientesVM = clientesVM;

            DataContext = this;

            FechaEntradaPicker.SelectedDateChanged += Fechas_SelectedDateChanged;
            FechaSalidaPicker.SelectedDateChanged += Fechas_SelectedDateChanged;

            CargarDatosAsync();
        }

        private async void CargarDatosAsync()
        {
            await _habitacionesVM.CargarHabitaciones();
            await _reservasVM.CargarReservasAsync();
            await _clientesVM.CargarClientesAsync();

            ActualizarHabitacionesDisponibles();
        }

        private void Fechas_SelectedDateChanged(object sender, SelectionChangedEventArgs e)
        {
            ActualizarHabitacionesDisponibles();
        }

        private void ActualizarHabitacionesDisponibles()
        {
            HabitacionesDisponibles.Clear();
            if (!FechaEntradaPicker.SelectedDate.HasValue || !FechaSalidaPicker.SelectedDate.HasValue)
                return;

            DateTime entrada = FechaEntradaPicker.SelectedDate.Value;
            DateTime salida = FechaSalidaPicker.SelectedDate.Value;

            if (salida <= entrada) return;

            var disponibles = _habitacionesVM.Habitaciones
                .Where(h => !_reservasVM.Reservas.Any(r =>
                    r.HabitacionId == h.Id &&
                    !(salida <= r.FechaEntrada || entrada >= r.FechaSalida)
                ));

            foreach (var h in disponibles)
                HabitacionesDisponibles.Add(h);
        }

        private async void Crear_Click(object sender, RoutedEventArgs e)
        {
            if (!FechaEntradaPicker.SelectedDate.HasValue || !FechaSalidaPicker.SelectedDate.HasValue)
            {
                MessageBox.Show("Selecciona las fechas");
                return;
            }

            if (!int.TryParse(TextBoxPersonas.Text, out int personas))
            {
                MessageBox.Show("Número de personas inválido");
                return;
            }

            if (ComboBoxHabitacion.SelectedItem is not Habitacion habitacion)
            {
                MessageBox.Show("Selecciona una habitación");
                return;
            }

            string dni = DniTextBox.Text.Trim();
            if (string.IsNullOrWhiteSpace(dni))
            {
                MessageBox.Show("Ingresa el DNI del cliente");
                return;
            }

            // 🔑 Buscar cliente en la lista de clientes cargada
            var clienteExistente = _clientesVM.Clientes
                .FirstOrDefault(c => c.Dni == dni);

            if (clienteExistente == null)
            {
                MessageBox.Show("El cliente no está registrado");
                return;
            }

            var dias = (FechaSalidaPicker.SelectedDate.Value - FechaEntradaPicker.SelectedDate.Value).TotalDays;

            // Crear reserva
            var reserva = new Reserva
            {
                Id = Guid.NewGuid().ToString(),
                ClienteId = clienteExistente.Id,
                Cliente = clienteExistente,
                HabitacionId = habitacion.Id,
                FechaEntrada = FechaEntradaPicker.SelectedDate.Value,
                FechaSalida = FechaSalidaPicker.SelectedDate.Value,
                Personas = Math.Min(personas, habitacion.MaxOcupantes),
                PrecioTotal = habitacion.PrecioNoche * (decimal)dias, // ⚡ Conversión a decimal
                Cancelacion = false
            };

            bool exito = await _reservasVM.AgregarReservaAsync(reserva);

            if (exito)
            {
                MessageBox.Show("Reserva creada correctamente");
                Close();
            }
            else
            {
                MessageBox.Show("Error creando la reserva");
            }
        }

        private void Cancelar_Click(object sender, RoutedEventArgs e)
        {
            Close();
        }
    }
}
