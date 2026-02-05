using HOTELINTERFAZ.Models;
using HOTELINTERFAZ.ViewModels;
using System;
using System.Collections.Generic;
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

namespace HOTELINTERFAZ.Views
{
    /// <summary>
    /// Lógica de interacción para LogIn.xaml
    /// </summary>
    public partial class LogIn : Window
    {
        public LogIn()
        {
            InitializeComponent();
        }

        private async Task LogIn_ButtonAsync(object sender, RoutedEventArgs e)
        {
            String email = txtEmailLogIn.Text;
            String password = txtPassLogIn.Password;
            try
            {
                Autenticacion auth = new Autenticacion();

                var resultado = await auth.Login(
                    email,
                    password
                );

                if (resultado.usuario.tipoUsuario != "Empleado")
                {
                    MessageBox.Show("Solo empleados pueden acceder");
                    return;
                }

                SessionManager.Token = resultado.Token;
                SessionManager.UsuarioActual = resultado.usuario;

                MainWindow main = new MainWindow();
                main.Show();
                this.Close();
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }
    }
}
