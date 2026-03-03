import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.hura.HuraApplication
import com.example.hura.data.repository.TransactionRepository
import com.example.hura.domain.model.ParsedTransaction
import com.example.hura.domain.model.TransactionType
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant

class TransactionsTestViewModel(app: Application) : AndroidViewModel(app) {

    // Grab repository directly from the Application singleton
    private val repository: TransactionRepository =
        (app as HuraApplication).transactionRepository

    val transactions: StateFlow<List<ParsedTransaction>> = repository.observeAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )

    fun addDummyTransaction() {
        val dummy = ParsedTransaction(
            amount = (1..500).random().toBigDecimal(),
            currency = "MKD",
            timestamp = Instant.now(),
            type = TransactionType.EXPENSE,
            merchant = "Vero",
            sourcePackage = "com.kuracpalac",
            notificationKey = (1..200000).random().toString()
        )
        viewModelScope.launch {
            repository.insert(dummy)
        }
    }
}