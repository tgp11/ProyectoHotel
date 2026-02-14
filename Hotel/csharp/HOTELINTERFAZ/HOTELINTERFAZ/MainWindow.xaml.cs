using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using HOTELINTERFAZ.Views;
using HOTELINTERFAZ.Models;

namespace HOTELINTERFAZ
{
    public partial class Principal : Window
    {
        private Usuario usuario;
        public Principal(Usuario _usuario)
        {
            InitializeComponent();
            usuario = _usuario;

            lblNombreUsuario.Content = usuario.Nombre;

            if (usuario.Administrador)
            {
                lblAdminEmp.Content = "Administrador";
            }
            else
            {
                lblAdminEmp.Content = "Empleado";
            }
        }

        // ================== CLIENTES ==================
        private void GestionUsuarios_Click(object sender, RoutedEventArgs e)
        {
            contentControl.Content = new ClienteView();
        }

        // ================== EMPLEADOS ==================
        private void GestionEmpleados_Click(object sender, RoutedEventArgs e)
        {
            if (usuario.Administrador)
            {
                contentControl.Content = new EmpleadosView();
            }
            else
            {
                MessageBox.Show("Solo los Administradores tinen acceso a los empleados.");
            }
            
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
            HOTELINTERFAZ.Views.LogIn login  =  new LogIn();
            login.Show();
            this.Close();
            
        }
    }
}
