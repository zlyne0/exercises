package promitech.ecc.param

import com.fasterxml.jackson.annotation.JsonValue
import java.io.Serializable

data class ParamId(@JsonValue val value: Long): Serializable