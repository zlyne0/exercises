package promitech.ecc.param

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
internal class ParamController(
    val paramService: ParamService
) {

    @RequestMapping(path = ["/paramCreate"], produces = ["application/json"])
    @ResponseBody
    fun create(): ParamDto {
        val param = paramService.createOne()
        return map(param)
    }

    @RequestMapping(path = ["/param"], produces= ["application/json"])
    @ResponseBody
    fun list(
        @RequestParam(name = "name", required = false) paramName: ParamName?,
        @RequestParam(name = "id", required = false) id: ParamId?
    ): List<ParamDto> {
        if (paramName != null) {
            return paramService.findAll(paramName)
                .map { entity -> map(entity) }
        }
        if (id != null) {
            return paramService.findAll(id)
                .map { entity -> map(entity) }
        }
        return paramService.findAll()
            .map { entity -> map(entity) }
    }

    private fun map(param: Param): ParamDto {
        return ParamDto(param.id!!, param.name, param.value)
    }
}