package com.dev_bayan_ibrahim.brc_shifting.data_source.local.local_database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.dev_bayan_ibrahim.brc_shifting.domain.model.Bonus
import com.dev_bayan_ibrahim.brc_shifting.domain.model.BonusType
import com.dev_bayan_ibrahim.brc_shifting.domain.model.util.SimpleDate
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Entity(
    tableName = BonusEntity.BONUS_TABLE,
    foreignKeys = [ForeignKey(
        entity = EmployeeEntity::class,
        parentColumns = [EmployeeEntity.EMPLOYEE_NUMBER],
        childColumns = [BonusEntity.BONUS_EMPLOYEE_NUMBER],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE,
    )],
    indices = [
        Index(value = [BonusEntity.BONUS_EMPLOYEE_NUMBER]),
        // this index to make sure that bonus are not dublicated cause we can't check the id cause it is local and remote fetched bonus
        // are without that id
        Index(
            value = [
                BonusEntity.BONUS_DATE,
                BonusEntity.BONUS_TYPE,
                BonusEntity.BONUS_TOTAL,
                BonusEntity.BONUS_NET
            ],
            unique = true
        )
    ]
)
data class BonusEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = BONUS_ID)
    val id: Long?,
    @ColumnInfo(name = BONUS_EMPLOYEE_NUMBER)
    val employeeNumber: Int,
    @ColumnInfo(name = BONUS_DATE)
    val date: SimpleDate,
    @ColumnInfo(name = BONUS_TYPE)
    val type: BonusType,
    @ColumnInfo(name = BONUS_TOTAL)
    val total: Int,
    @ColumnInfo(name = BONUS_NET)
    val net: Int,
    @ColumnInfo(name = BONUS_CREATED_AT)
    val createdAt: Instant,
    @ColumnInfo(name = BONUS_UPDATED_AT)
    val updatedAt: Instant,
) {
    companion object {
        const val BONUS_TABLE = "bonus"
        const val BONUS_ID = "bonus_id"
        const val BONUS_EMPLOYEE_NUMBER = "bonus_employee_number"
        const val BONUS_DATE = "bonus_date"
        const val BONUS_TYPE = "bonus_type"
        const val BONUS_TOTAL = "bonus_total"
        const val BONUS_NET = "bonus_net"
        const val BONUS_CREATED_AT = "bonus_created_at"
        const val BONUS_UPDATED_AT = "bonus_updated_at"
    }
}

// Updated Mappers
fun BonusEntity.toBonus() = Bonus(
    id = id,
    employeeNumber = employeeNumber,
    date = date,
    type = type,
    total = total,
    net = net,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun Bonus.toEntity(overrideUpdatedAt: Boolean = true, overrideUpdatedAtIfNew: Boolean = false) = BonusEntity(
    id = id,
    employeeNumber = employeeNumber,
    date = date,
    type = type,
    total = total,
    net = net,
    createdAt = createdAt,
    updatedAt = if (overrideUpdatedAt && (overrideUpdatedAtIfNew || id != null)) Clock.System.now() else updatedAt
)