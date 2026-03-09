import androidx.room.Embedded
import androidx.room.ColumnInfo
import androidx.room.Relation
import com.example.hura.data.local.entity.TransactionEntity
import com.example.hura.data.local.entity.MerchantEntity
import com.example.hura.data.local.entity.CategoryEntity

data class TransactionWithMerchantAndCategory(
    val transaction_id: Long,
    val amount: String,
    val currency: String,
    val merchantId: Long,
    val bankName: String,
    val timestamp: Long,
    val notificationKey: String?,
    val type: String,
    val isDeleted: Boolean,

    val merchant_id: Long,
    val merchant_rawName: String,
    val merchant_nickname: String?,
    val merchant_categoryId: Long?,

    val category_id: Long?,
    val category_name: String?,
    val category_iconId: Int?
)