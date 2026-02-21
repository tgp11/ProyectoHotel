import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacion_hotel.Repository.AuthRepository
import com.example.aplicacion_hotel.Repository.ClienteRepository
import com.example.aplicacion_hotel.utils.httpErrorMessage
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class AuthViewModel(
    private val hotelSessionManager: com.example.aplicacion_hotel.utils.HotelSessionManager
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    private val repository = AuthRepository()
    private val clienteRepository = ClienteRepository()

    var loginSuccess by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun login(email: String, password: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = repository.login(email, password)

                if (response.usuario.tipoUsuario != "Cliente") {
                    errorMessage = "Solo los clientes pueden iniciar sesión"
                    return@launch
                }

                hotelSessionManager.saveToken(response.token)

                val clienteCompleto = clienteRepository.getClienteById(response.usuario.id)
                hotelSessionManager.saveCliente(clienteCompleto)

                loginSuccess = true

            } catch (e: HttpException) {
                errorMessage = httpErrorMessage(e)
                Log.e("LOGIN_ERROR", "HTTP ${e.code()} -> ${errorMessage}")
            } catch (e: IOException) {
                errorMessage = "Error de red. Revisa tu conexión."
                Log.e("LOGIN_ERROR", "Network error", e)
            } catch (e: Exception) {
                errorMessage = "Error inesperado: ${e.message}"
                Log.e("LOGIN_ERROR", "Unknown error", e)
            } finally {
                isLoading = false
            }
        }
    }
}

