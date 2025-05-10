package com.dev_bayan_ibrahim.brc_shifting.domain.model.exception

sealed class DataConflictException(override val message: String? = null) : Exception()

sealed class EmployeeDataConflictException(override val message: String? = null) : DataConflictException(message)
data class EmployeeInsertDataConflictException(override val message: String? = null) : EmployeeDataConflictException(message)
data class EmployeeDeleteDataConflictException(override val message: String? = null) : EmployeeDataConflictException(message)


sealed class SalaryDataConflictException(override val message: String? = null) : DataConflictException(message)
data class SalaryInsertDataConflictException(override val message: String? = null) : SalaryDataConflictException(message)
data class SalaryDeleteDataConflictException(override val message: String? = null) : SalaryDataConflictException(message)
