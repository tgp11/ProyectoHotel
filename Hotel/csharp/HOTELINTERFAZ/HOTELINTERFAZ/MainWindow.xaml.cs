using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows;
using HOTELINTERFAZ.Views;

namespace InterfazInterna
{
    public partial class Principal : Window
    {
        public Principal()
        {
            InitializeComponent();
        }

        // ================== CLIENTES ==================
        private void GestionUsuarios_Click(object sender, RoutedEventArgs e)
        {
            contentControl.Content = new ReservasView();
        }

        // ================== EMPLEADOS ==================
        private void GestionEmpleados_Click(object sender, RoutedEventArgs e)
        {
            contentControl.Content = new ReservasView();
        }

        // ================== HABITACIONES ==================
        private void GestionHabitaciones_Click(object sender, RoutedEventArgs e)
        {
            contentControl.Content = new HabitacionesView();
        }

        // ================== RESERVAS ==================
        private void GestionReservas_Click(object sender, RoutedEventArgs e)
        {
            contentControl.Content = new ReservasView();
        }

        // ================== RESEÑAS ==================
        private void GestionResenas_Click(object sender, RoutedEventArgs e)
        {
            contentControl.Content = new ReservasView();
        }

        // ================== LOGOUT ==================
        private void CerrarSesion_Click(object sender, RoutedEventArgs e)
        {
            var login = new ReservasView();
            this.Close();
        }
    }
}
