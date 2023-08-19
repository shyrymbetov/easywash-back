package kz.innlab.fileservice.controller

import kz.innlab.fileservice.dto.Status
import kz.innlab.fileservice.service.FileService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.InputStreamResource
import org.springframework.http.*
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.FileInputStream
import java.nio.charset.StandardCharsets
import java.util.*

/**
 * @project edm-spring
 * @author Bekzat Sailaubayev on 12.02.2022
 */

@RestController
class FileController {

    @Autowired
    lateinit var fileService: FileService

    @GetMapping("/download/{id}")
    @PreAuthorize("isAuthenticated()")
    fun getDownloadFile(
        @PathVariable(value = "id") id: UUID,
        @RequestParam(value = "filename") filename: String,
        authentication: Authentication
    ): ResponseEntity<*> {
        val file = fileService.getFile(id)
        if (file.isEmpty) {
            return ResponseEntity(null, HttpStatus.BAD_GATEWAY)
        }
        val pathToFile = fileService.getFullPath(file.get())
        if (FileService.fileExists(pathToFile)) {
            val resource = InputStreamResource(FileInputStream(pathToFile))

            val httpHeaders = HttpHeaders()
            val contentDisposition = ContentDisposition.builder("attachment")
                .filename("$filename.${file.get().fileFormat}", StandardCharsets.UTF_8)
                .build()
            httpHeaders.contentDisposition = contentDisposition
            httpHeaders.contentType = FileService.contentTypeFromMime(file.get().mime, MediaType.APPLICATION_OCTET_STREAM)
            if (file.get().size != null && file.get().size!! > 0) {
                httpHeaders.contentLength = file.get().size!!
            }
            return ResponseEntity(resource, httpHeaders, HttpStatus.OK)
        }

        return ResponseEntity("File not exists", HttpStatus.BAD_REQUEST)
    }

    @PostMapping("upload")
    @PreAuthorize("isAuthenticated()")
    fun uploadFile(
        @RequestParam("file") fileForm: MultipartFile,
        authentication: Authentication
    ): ResponseEntity<*> {
        val status = Status()
        val result = fileService.saveFile(fileForm)
        if (result != null) {
            status.status = 1
            status.message = "OK"
            status.value = result
        }
        return ResponseEntity(status, HttpStatus.OK)
    }

}
