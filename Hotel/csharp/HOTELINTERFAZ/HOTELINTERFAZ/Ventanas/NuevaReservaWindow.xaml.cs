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
        // Todas las habitaciones y reservas
        private ObservableCollection<Habitacion> _todasHabitaciones = new();
        private ObservableCollection<Reserva> _todasReservas = new();

        // Colección filtrada para el ComboBox
        public ObservableCollection<Habitacion> HabitacionesDisponibles { get; set; } = new();

        public NuevaReservaWindow()
        {
            InitializeComponent();

            DataContext = this;

            ConfigurarCalendario();

            // Cargar datos asíncronamente
            CargarDatosAsync();

            // Actualizar habitaciones al cambiar fechas
            FechaEntradaPicker.SelectedDateChanged += Fechas_SelectedDateChanged;
            FechaSalidaPicker.SelectedDateChanged += Fechas_SelectedDateChanged;
        }

        private async void CargarDatosAsync()
        {
            try
            {
                var habitacionesVM = new HabitacionesViewModel();
                var reservasVM = new ReservasViewModel();

                // Esperar a que cargue la API
                await habitacionesVM.CargarHabitaciones();
                await reservasVM.CargarReservas();

                _todasHabitaciones = habitacionesVM.Habitaciones;
                _todasReservas = reservasVM.Reservas;

                // Filtrar habitaciones disponibles
                ActualizarHabitacionesDisponibles();
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error al cargar datos: " + ex.Message);
            }
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

        #region Configuración calendario
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
        #endregion

        private void Crear_Click(object sender, RoutedEventArgs e)
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

            if (ComboBoxHabitacion.SelectedItem == null)
            {
                MessageBox.Show("Selecciona una habitación disponible");
                return;
            }

            // Aquí guardarías la reserva usando API o ViewModel
            MessageBox.Show("Reserva creada correctamente");
            Close();
        }

        private void Cancelar_Click(object sender, RoutedEventArgs e)
        {
            Close();
        }
    }
}
