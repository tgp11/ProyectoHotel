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

        private ObservableCollection<Habitacion> _todasHabitaciones = new();
        private ObservableCollection<Reserva> _todasReservas = new();

        public ObservableCollection<Habitacion> HabitacionesDisponibles { get; set; } = new();

        public NuevaReservaWindow(ReservasViewModel reservasVM, HabitacionesViewModel habitacionesVM)
        {
            InitializeComponent();

            _reservasVM = reservasVM;
            _habitacionesVM = habitacionesVM;

            DataContext = this;

            ConfigurarCalendario();

            FechaEntradaPicker.SelectedDateChanged += Fechas_SelectedDateChanged;
            FechaSalidaPicker.SelectedDateChanged += Fechas_SelectedDateChanged;

            CargarDatosAsync();
        }

        private async void CargarDatosAsync()
        {
            await _habitacionesVM.CargarHabitaciones();
            await _reservasVM.CargarReservas();

            _todasHabitaciones = _habitacionesVM.Habitaciones;
            _todasReservas = _reservasVM.Reservas;

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

            if (salida <= entrada)
                return;

            var disponibles = _todasHabitaciones
                .Where(h => !_todasReservas.Any(r =>
                    r.HabitacionId == h.Id &&
                    !(salida <= r.FechaEntrada || entrada >= r.FechaSalida)
                ));

            foreach (var h in disponibles)
                HabitacionesDisponibles.Add(h);
        }

        private void ConfigurarCalendario()
        {
            DateTime ahora = DateTime.Now;
            DateTime fechaMinima = ahora.Hour < 4 ? DateTime.Today.AddDays(-1) : DateTime.Today;

            BloquearFechasAnteriores(FechaEntradaPicker, fechaMinima);
            BloquearFechasAnteriores(FechaSalidaPicker, fechaMinima);

            FechaEntradaPicker.SelectedDate = fechaMinima;
            FechaSalidaPicker.SelectedDate = fechaMinima.AddDays(1);
        }

        private void BloquearFechasAnteriores(DatePicker picker, DateTime fechaMinima)
        {
            picker.BlackoutDates.Clear();
            picker.BlackoutDates.Add(new CalendarDateRange(DateTime.MinValue, fechaMinima.AddDays(-1)));
            picker.DisplayDateStart = fechaMinima;
        }

        private async void Crear_Click(object sender, RoutedEventArgs e)
        {
            if (FechaEntradaPicker.SelectedDate == null ||
                FechaSalidaPicker.SelectedDate == null)
            {
                MessageBox.Show("Selecciona las fechas");
                return;
            }

            if (FechaSalidaPicker.SelectedDate <= FechaEntradaPicker.SelectedDate)
            {
                MessageBox.Show("La fecha de salida debe ser posterior a la de entrada");
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

            // Limitar personas al máximo de la habitación
            if (personas > habitacion.MaxOcupantes)
                personas = habitacion.MaxOcupantes;

            string dni = DniTextBox.Text.Trim();
            if (string.IsNullOrWhiteSpace(dni))
            {
                MessageBox.Show("Ingresa el DNI del cliente");
                return;
            }

            // Buscar si el cliente ya existe en reservas
            var clienteExistente = _reservasVM.Reservas
                .Select(r => r.Cliente)
                .FirstOrDefault(c => c != null && c.Dni == dni);

            if (clienteExistente == null)
            {
                MessageBox.Show("El cliente no está dado de alta");
                return;
            }

            // Crear reserva
            var reserva = new Reserva
            {
                Id = Guid.NewGuid().ToString(),
                ClienteId = clienteExistente.Id,
                HabitacionId = habitacion.Id,
                FechaEntrada = FechaEntradaPicker.SelectedDate.Value,
                FechaSalida = FechaSalidaPicker.SelectedDate.Value,
                Personas = personas,
                PrecioTotal = (double)habitacion.PrecioNoche * (FechaSalidaPicker.SelectedDate.Value - FechaEntradaPicker.SelectedDate.Value).Days,
                Cancelacion = false,
                Cliente = clienteExistente
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
