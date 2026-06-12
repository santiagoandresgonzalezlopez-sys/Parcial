package com.parcial.app.controlador;

import com.parcial.app.model.*;
import com.parcial.app.model.Usuario.Rol;
import com.parcial.app.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class WebController {

    @Autowired private DirectorRepository           directorRepo;
    @Autowired private DocenteRepository            docenteRepo;
    @Autowired private FacultadRepository           facultadRepo;
    @Autowired private AlumnoRepository             alumnoRepo;
    @Autowired private PagoRepository               pagoRepo;
    @Autowired private ResultadoSaberProRepository  resultadoTotalRepo;
    @Autowired private UsuarioRepository            usuarioRepo;

    // ══════════════════════════════════════════════════════════════════════════
    // LOGIN
    // ══════════════════════════════════════════════════════════════════════════

    @GetMapping("/")
    public String loginForm() { return "login"; }

    @PostMapping("/login")
    public String doLogin(@RequestParam String email,
                          @RequestParam String password,
                          HttpSession session, Model model) {

        Optional<Usuario> opt = usuarioRepo.findByEmail(email.trim().toLowerCase());
        if (opt.isEmpty()) {
            model.addAttribute("error", "Correo o contraseña incorrectos.");
            return "login";
        }
        Usuario u = opt.get();
        if (!u.isActivo()) {
            model.addAttribute("error", "Tu cuenta está desactivada. Contacta al administrador.");
            return "login";
        }
        if (!u.getPassword().equals(password)) {
            model.addAttribute("error", "Correo o contraseña incorrectos.");
            return "login";
        }
        session.setAttribute("usuarioId",     u.getId());
        session.setAttribute("usuarioEmail",  u.getEmail());
        session.setAttribute("usuarioNombre", u.getNombre());
        session.setAttribute("rol",           u.getRol().name());
        session.setAttribute("cedula",        u.getCedula());

        return switch (u.getRol()) {
            case ADMIN       -> "redirect:/admin/dashboard";
            case COORDINADOR -> "redirect:/coordinador/dashboard";
            case DOCENTE     -> "redirect:/docente/dashboard";
            case ESTUDIANTE  -> "redirect:/estudiante/dashboard";
        };
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // ══════════════════════════════════════════════════════════════════════════
    // ADMIN
    // ══════════════════════════════════════════════════════════════════════════

    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session) {
        if (!tieneRol(session, "ADMIN")) return "redirect:/";
        return "admin/dashboard";
    }

    @GetMapping("/admin/directores")
    public String listDirectores(Model model, HttpSession session) {
        if (!tieneRol(session, "ADMIN")) return "redirect:/";
        model.addAttribute("directores", directorRepo.findAll());
        model.addAttribute("director", new Director());
        return "admin/directores";
    }

    @PostMapping("/admin/directores/guardar")
    public String saveDirector(@ModelAttribute Director director) {
        directorRepo.save(director);
        return "redirect:/admin/directores";
    }

    @GetMapping("/admin/directores/editar/{id}")
    public String editDirector(@PathVariable Long id, Model model) {
        model.addAttribute("director", directorRepo.findById(id).orElse(new Director()));
        model.addAttribute("directores", directorRepo.findAll());
        return "admin/directores";
    }

    @GetMapping("/admin/directores/eliminar/{id}")
    public String deleteDirector(@PathVariable Long id) {
        directorRepo.deleteById(id);
        return "redirect:/admin/directores";
    }

    @GetMapping("/admin/docentes")
    public String listDocentes(Model model, HttpSession session) {
        if (!tieneRol(session, "ADMIN")) return "redirect:/";
        model.addAttribute("docentes", docenteRepo.findAll());
        model.addAttribute("docente", new Docente());
        model.addAttribute("facultades", facultadRepo.findAll());
        return "admin/docentes";
    }

    @PostMapping("/admin/docentes/guardar")
    public String saveDocente(@ModelAttribute Docente docente) {
        docenteRepo.save(docente);
        if (docente.getEmail() != null && !usuarioRepo.existsByEmail(docente.getEmail())) {
            usuarioRepo.save(new Usuario(
                docente.getEmail().trim().toLowerCase(), docente.getCedula(),
                Rol.DOCENTE, docente.getCedula(), docente.getNombre()
            ));
        }
        return "redirect:/admin/docentes";
    }

    @GetMapping("/admin/docentes/editar/{id}")
    public String editDocente(@PathVariable Long id, Model model) {
        model.addAttribute("docente", docenteRepo.findById(id).orElse(new Docente()));
        model.addAttribute("docentes", docenteRepo.findAll());
        model.addAttribute("facultades", facultadRepo.findAll());
        return "admin/docentes";
    }

    @GetMapping("/admin/docentes/eliminar/{id}")
    public String deleteDocente(@PathVariable Long id) {
        docenteRepo.deleteById(id);
        return "redirect:/admin/docentes";
    }

    @GetMapping("/admin/facultades")
    public String listFacultades(Model model, HttpSession session) {
        if (!tieneRol(session, "ADMIN")) return "redirect:/";
        model.addAttribute("facultades", facultadRepo.findAll());
        model.addAttribute("facultad", new Facultad());
        return "admin/facultades";
    }

    @PostMapping("/admin/facultades/guardar")
    public String saveFacultad(@ModelAttribute Facultad facultad) {
        facultadRepo.save(facultad);
        return "redirect:/admin/facultades";
    }

    @GetMapping("/admin/facultades/editar/{id}")
    public String editFacultad(@PathVariable Long id, Model model) {
        model.addAttribute("facultad", facultadRepo.findById(id).orElse(new Facultad()));
        model.addAttribute("facultades", facultadRepo.findAll());
        return "admin/facultades";
    }

    @GetMapping("/admin/facultades/eliminar/{id}")
    public String deleteFacultad(@PathVariable Long id) {
        facultadRepo.deleteById(id);
        return "redirect:/admin/facultades";
    }

    @GetMapping("/admin/usuarios")
    public String listUsuarios(Model model, HttpSession session) {
        if (!tieneRol(session, "ADMIN")) return "redirect:/";
        model.addAttribute("usuarios", usuarioRepo.findAll());
        model.addAttribute("roles", Rol.values());
        model.addAttribute("usuario", new Usuario());
        return "admin/usuarios";
    }

    @PostMapping("/admin/usuarios/guardar")
    public String saveUsuario(@ModelAttribute Usuario usuario) {
        if (isBlank(usuario.getPassword())) usuario.setPassword(usuario.getCedula());
        if (usuario.getEmail() != null) usuario.setEmail(usuario.getEmail().trim().toLowerCase());
        usuarioRepo.save(usuario);
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/admin/usuarios/editar/{id}")
    public String editUsuario(@PathVariable Long id, Model model, HttpSession session) {
        if (!tieneRol(session, "ADMIN")) return "redirect:/";
        model.addAttribute("usuario", usuarioRepo.findById(id).orElse(new Usuario()));
        model.addAttribute("usuarios", usuarioRepo.findAll());
        model.addAttribute("roles", Rol.values());
        return "admin/usuarios";
    }

    @GetMapping("/admin/usuarios/eliminar/{id}")
    public String deleteUsuario(@PathVariable Long id) {
        usuarioRepo.deleteById(id);
        return "redirect:/admin/usuarios";
    }

    @PostMapping("/admin/usuarios/toggle/{id}")
    public String toggleUsuario(@PathVariable Long id) {
        usuarioRepo.findById(id).ifPresent(u -> { u.setActivo(!u.isActivo()); usuarioRepo.save(u); });
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/admin/resolucion")
    public String resolucionAdmin(Model model) {
        model.addAttribute("rangos", getRangosBeneficios());
        model.addAttribute("pdfLink", PDF_LINK);
        return "admin/resolucion_beneficios";
    }

    // ══════════════════════════════════════════════════════════════════════════
    // COORDINADOR
    // ══════════════════════════════════════════════════════════════════════════

    @GetMapping("/coordinador/dashboard")
    public String coordinadorDashboard(HttpSession session) {
        if (!tieneRol(session, "COORDINADOR")) return "redirect:/";
        return "coordinador/dashboard";
    }

    @GetMapping("/coordinador/alumnos")
    public String listAlumnos(Model model, HttpSession session) {
        if (!tieneRol(session, "COORDINADOR")) return "redirect:/";
        model.addAttribute("alumnos",        alumnoRepo.findAll());
        model.addAttribute("alumno",         new Alumno());
        model.addAttribute("facultades",     facultadRepo.findAll());
        model.addAttribute("tiposDocumento", TIPOS_DOC);
        return "coordinador/alumnos";
    }

    @PostMapping("/coordinador/alumnos/guardar")
    public String saveAlumno(@ModelAttribute Alumno alumno, Model model, HttpSession session) {
        if (!tieneRol(session, "COORDINADOR")) return "redirect:/";

        String reg = alumno.getNumeroRegistro();
        if (reg != null && !reg.isBlank()) {
            if (!reg.matches("[A-Za-z]{2}\\d{11}")) {
                return alumnoError(model, alumno, "El número de registro debe tener 2 letras + 11 dígitos (Ej: EK20183007722).");
            }
            boolean dup = alumno.getId() == null
                ? alumnoRepo.existsByNumeroRegistro(reg)
                : alumnoRepo.existsByNumeroRegistroAndIdNot(reg, alumno.getId());
            if (dup) return alumnoError(model, alumno, "El número de registro '" + reg + "' ya está en uso.");
        }

        boolean cedulaDup = alumno.getId() == null
            ? alumnoRepo.existsByCedula(alumno.getCedula())
            : alumnoRepo.existsByCedulaAndIdNot(alumno.getCedula(), alumno.getId());
        if (cedulaDup) return alumnoError(model, alumno, "Ya existe un alumno con esa cédula.");

        alumnoRepo.save(alumno);

        String emailAcceso = alumno.getCedula() + "@estudiante.edu.co";
        if (!usuarioRepo.existsByEmail(emailAcceso)) {
            usuarioRepo.save(new Usuario(emailAcceso, alumno.getCedula(),
                Rol.ESTUDIANTE, alumno.getCedula(), alumno.getNombre()));
        }
        return "redirect:/coordinador/alumnos";
    }

    private String alumnoError(Model model, Alumno alumno, String msg) {
        model.addAttribute("errorForm",      msg);
        model.addAttribute("alumno",         alumno);
        model.addAttribute("alumnos",        alumnoRepo.findAll());
        model.addAttribute("facultades",     facultadRepo.findAll());
        model.addAttribute("tiposDocumento", TIPOS_DOC);
        return "coordinador/alumnos";
    }

    @GetMapping("/coordinador/alumnos/editar/{id}")
    public String editAlumno(@PathVariable Long id, Model model) {
        model.addAttribute("alumno",         alumnoRepo.findById(id).orElse(new Alumno()));
        model.addAttribute("alumnos",        alumnoRepo.findAll());
        model.addAttribute("facultades",     facultadRepo.findAll());
        model.addAttribute("tiposDocumento", TIPOS_DOC);
        return "coordinador/alumnos";
    }

    @GetMapping("/coordinador/alumnos/eliminar/{id}")
    public String deleteAlumno(@PathVariable Long id) {
        alumnoRepo.deleteById(id);
        return "redirect:/coordinador/alumnos";
    }

    @GetMapping("/coordinador/aprobar-pago")
    public String aprobarPagoView(Model model, HttpSession session) {
        if (!tieneRol(session, "COORDINADOR")) return "redirect:/";
        model.addAttribute("pagos", pagoRepo.findAll());
        return "coordinador/aprobar_pago";
    }

    @PostMapping("/coordinador/aprobar-pago/{id}")
    public String aprobarPago(@PathVariable Long id) {
        Pago pago = pagoRepo.findById(id).orElseThrow();
        pago.setAprobado(true);
        pago.setFechaAprobacion(LocalDateTime.now());
        pagoRepo.save(pago);
        alumnoRepo.findByCedula(pago.getCedulaAlumno()).ifPresent(a -> {
            a.setPagoAprobado(true); alumnoRepo.save(a);
        });
        return "redirect:/coordinador/aprobar-pago";
    }

    @PostMapping("/coordinador/rechazar-pago/{id}")
    public String rechazarPago(@PathVariable Long id) {
        pagoRepo.deleteById(id);
        return "redirect:/coordinador/aprobar-pago";
    }

    @GetMapping("/coordinador/informe-beneficios")
    public String informeBeneficiosCoord(Model model, HttpSession session) {
        if (!tieneRol(session, "COORDINADOR")) return "redirect:/";
        model.addAttribute("informe", buildInforme());
        model.addAttribute("rangos",  getRangosBeneficios());
        model.addAttribute("pdfLink", PDF_LINK);
        return "coordinador/informe_beneficios";
    }

    // ── Resultados ────────────────────────────────────────────────────────────

    @GetMapping("/coordinador/resultado/{alumnoId}")
    public String registrarResultadoForm(@PathVariable Long alumnoId,
                                         Model model, HttpSession session) {
        if (!tieneRol(session, "COORDINADOR")) return "redirect:/";
        Alumno alumno = alumnoRepo.findById(alumnoId)
            .orElseThrow(() -> new RuntimeException("Alumno no encontrado: " + alumnoId));
        ResultadoSaberPro resultado = resultadoTotalRepo.findByAlumno(alumno)
            .orElseGet(ResultadoSaberPro::new);
        model.addAttribute("alumno",            alumno);
        model.addAttribute("resultado",         resultado);
        model.addAttribute("nivelesGenericos",  List.of("Nivel 1","Nivel 2","Nivel 3","Nivel 4"));
        model.addAttribute("nivelesInglesIcfes",List.of("A-","A1","A2","B1","B+"));
        model.addAttribute("nivelesMarco",      List.of("A0","A1","A2","B1","B2","C1","C2"));
        return "coordinador/registrar_resultado";
    }

    @PostMapping("/coordinador/resultado/{alumnoId}")
    public String guardarResultado(@PathVariable Long alumnoId,
                                   @ModelAttribute ResultadoSaberPro resultado,
                                   HttpSession session) {
        if (!tieneRol(session, "COORDINADOR")) return "redirect:/";
        Alumno alumno = alumnoRepo.findById(alumnoId)
            .orElseThrow(() -> new RuntimeException("Alumno no encontrado: " + alumnoId));
        resultadoTotalRepo.findByAlumno(alumno).ifPresent(r -> resultado.setId(r.getId()));
        resultado.setAlumno(alumno);

        if (resultado.isPruebaNula()) {
            resultado.setPuntajeGlobal(0);          resultado.setNivelDesempeno("ANULADO");
            resultado.setPercentil(0);
            resultado.setPuntajeComunicacionEscrita(0);        resultado.setNivelComunicacionEscrita("");
            resultado.setPuntajeRazonamientoCuantitativo(0);   resultado.setNivelRazonamientoCuantitativo("");
            resultado.setPuntajeLecturaCritica(0);             resultado.setNivelLecturaCritica("");
            resultado.setPuntajeCompetenciasCiudadanas(0);     resultado.setNivelCompetenciasCiudadanas("");
            resultado.setPuntajeIngles(0);                     resultado.setNivelIngles("");
            resultado.setNivelInglesMarco("");
            resultado.setPuntajeFormulacionProyectos(0);       resultado.setNivelFormulacionProyectos("");
            resultado.setPuntajePensamientoCientifico(0);      resultado.setNivelPensamientoCientifico("");
            resultado.setPuntajeDisenioSoftware(0);            resultado.setNivelDisenioSoftware("");
        } else {
            resultado.setNivelDesempeno(getNivelPorPuntaje(resultado.getPuntajeGlobal()));
            if (isBlank(resultado.getNivelComunicacionEscrita()))
                resultado.setNivelComunicacionEscrita(getNivelPorPuntaje(resultado.getPuntajeComunicacionEscrita()));
            if (isBlank(resultado.getNivelRazonamientoCuantitativo()))
                resultado.setNivelRazonamientoCuantitativo(getNivelPorPuntaje(resultado.getPuntajeRazonamientoCuantitativo()));
            if (isBlank(resultado.getNivelLecturaCritica()))
                resultado.setNivelLecturaCritica(getNivelPorPuntaje(resultado.getPuntajeLecturaCritica()));
            if (isBlank(resultado.getNivelCompetenciasCiudadanas()))
                resultado.setNivelCompetenciasCiudadanas(getNivelPorPuntaje(resultado.getPuntajeCompetenciasCiudadanas()));
            if (isBlank(resultado.getNivelIngles()))
                resultado.setNivelIngles(getNivelInglesIcfes(resultado.getPuntajeIngles()));
            if (isBlank(resultado.getNivelFormulacionProyectos()))
                resultado.setNivelFormulacionProyectos(getNivelPorPuntaje(resultado.getPuntajeFormulacionProyectos()));
            if (isBlank(resultado.getNivelPensamientoCientifico()))
                resultado.setNivelPensamientoCientifico(getNivelPorPuntaje(resultado.getPuntajePensamientoCientifico()));
            if (isBlank(resultado.getNivelDisenioSoftware()))
                resultado.setNivelDisenioSoftware(getNivelPorPuntaje(resultado.getPuntajeDisenioSoftware()));
        }
        resultadoTotalRepo.save(resultado);
        return "redirect:/coordinador/alumnos";
    }

    @GetMapping("/coordinador/resultado/eliminar/{alumnoId}")
    public String eliminarResultado(@PathVariable Long alumnoId, HttpSession session) {
        if (!tieneRol(session, "COORDINADOR")) return "redirect:/";
        alumnoRepo.findById(alumnoId).ifPresent(a ->
            resultadoTotalRepo.findByAlumno(a).ifPresent(resultadoTotalRepo::delete));
        return "redirect:/coordinador/alumnos";
    }

    // ══════════════════════════════════════════════════════════════════════════
    // DOCENTE
    // ══════════════════════════════════════════════════════════════════════════

    @GetMapping("/docente/dashboard")
    public String docenteDashboard(HttpSession session) {
        if (!tieneRol(session, "DOCENTE")) return "redirect:/";
        return "docente/dashboard";
    }

    @GetMapping("/docente/buscar-facultad")
    public String buscarPorFacultadForm(Model model, HttpSession session) {
        if (!tieneRol(session, "DOCENTE")) return "redirect:/";
        model.addAttribute("facultades", facultadRepo.findAll());
        return "docente/buscar_por_facultad";
    }

    @PostMapping("/docente/buscar-facultad")
    public String buscarPorFacultadResult(@RequestParam String facultad, Model model) {
        List<Map<String, Object>> resultados = new ArrayList<>();
        for (Alumno a : alumnoRepo.findByFacultad(facultad)) {
            Map<String, Object> item = new HashMap<>();
            item.put("alumno",    a);
            item.put("resultado", resultadoTotalRepo.findByAlumno(a).orElse(null));
            resultados.add(item);
        }
        model.addAttribute("resultados",           resultados);
        model.addAttribute("facultadSeleccionada", facultad);
        model.addAttribute("facultades",           facultadRepo.findAll());
        return "docente/buscar_por_facultad";
    }

    @GetMapping("/docente/buscar-cedula")
    public String buscarPorCedulaForm(HttpSession session) {
        if (!tieneRol(session, "DOCENTE")) return "redirect:/";
        return "docente/buscar_por_cedula";
    }

    @PostMapping("/docente/buscar-cedula")
    public String buscarPorCedulaResult(@RequestParam String cedula, Model model) {
        alumnoRepo.findByCedula(cedula).ifPresentOrElse(alumno -> {
            model.addAttribute("alumno",         alumno);
            model.addAttribute("resultadoTotal", resultadoTotalRepo.findByAlumno(alumno).orElse(null));
        }, () -> model.addAttribute("error", "No se encontró estudiante con cédula: " + cedula));
        return "docente/buscar_por_cedula";
    }

    @GetMapping("/docente/informe-beneficios")
    public String informeBeneficiosDocente(Model model, HttpSession session) {
        if (!tieneRol(session, "DOCENTE")) return "redirect:/";
        model.addAttribute("informe", buildInforme());
        model.addAttribute("pdfLink", PDF_LINK);
        model.addAttribute("rangos",  getRangosBeneficios());
        return "docente/informe_beneficios";
    }

    // ══════════════════════════════════════════════════════════════════════════
    // ESTUDIANTE
    // ══════════════════════════════════════════════════════════════════════════

    @GetMapping("/estudiante/dashboard")
    public String estudianteDashboard(HttpSession session, Model model) {
        if (!tieneRol(session, "ESTUDIANTE")) return "redirect:/";
        String cedula = (String) session.getAttribute("cedula");
        alumnoRepo.findByCedula(cedula).ifPresentOrElse(alumno -> {
            model.addAttribute("alumno",         alumno);
            model.addAttribute("pagoAprobado",   alumno.isPagoAprobado());
            model.addAttribute("tieneResultados",
                resultadoTotalRepo.findByAlumno(alumno).isPresent());
        }, () -> model.addAttribute("error", "Estudiante no encontrado"));
        return "estudiante/dashboard";
    }

    @GetMapping("/estudiante/cargar-pago")
    public String cargarPagoForm(HttpSession session, Model model) {
        if (!tieneRol(session, "ESTUDIANTE")) return "redirect:/";
        model.addAttribute("cedula", session.getAttribute("cedula"));
        return "estudiante/cargar_pago";
    }

    @PostMapping("/estudiante/cargar-pago")
    public String guardarPago(@RequestParam String cedula,
                              @RequestParam("archivo") MultipartFile archivo,
                              Model model) {

        model.addAttribute("cedula", cedula);

        if (pagoRepo.findByCedulaAlumnoAndAprobado(cedula, true).isPresent()) {
            model.addAttribute("error", "Ya tienes un pago aprobado.");
            return "estudiante/cargar_pago";
        }
        if (archivo == null || archivo.isEmpty()) {
            model.addAttribute("error", "Debes seleccionar un archivo.");
            return "estudiante/cargar_pago";
        }

        // Validar tipo
        String contentType  = archivo.getContentType() != null ? archivo.getContentType() : "";
        String originalName = archivo.getOriginalFilename() != null
                              ? archivo.getOriginalFilename().toLowerCase() : "archivo";
        boolean esPdf    = contentType.equals("application/pdf") || originalName.endsWith(".pdf");
        boolean esImagen = contentType.startsWith("image/");

        if (!esPdf && !esImagen) {
            model.addAttribute("error", "Solo se aceptan archivos PDF o imagenes (JPG, PNG).");
            return "estudiante/cargar_pago";
        }

        try {
            // Carpeta de destino: <proyecto>/uploads/pagos/
            Path uploadDir = Paths.get("uploads", "pagos");
            Files.createDirectories(uploadDir);

            // Nombre único para evitar colisiones
            String ext        = originalName.contains(".")
                                ? originalName.substring(originalName.lastIndexOf("."))
                                : ".pdf";
            String nombreGuardado = cedula + "_" + System.currentTimeMillis() + ext;
            Path destino = uploadDir.resolve(nombreGuardado);
            Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            Pago pago = new Pago(cedula, archivo.getOriginalFilename(),
                                 contentType, destino.toString());
            pagoRepo.save(pago);
            model.addAttribute("mensaje", "Comprobante enviado correctamente. Espera la aprobacion del coordinador.");

        } catch (IOException e) {
            model.addAttribute("error", "Error al guardar el archivo: " + e.getMessage());
        }
        return "estudiante/cargar_pago";
    }

    /** Sirve el archivo al coordinador para visualizarlo en el navegador. */
    @GetMapping("/coordinador/pago/archivo/{id}")
    public ResponseEntity<Resource> verArchivoPago(@PathVariable Long id) {
        return pagoRepo.findById(id).map(pago -> {
            if (pago.getRutaArchivo() == null) return ResponseEntity.notFound().<Resource>build();

            Path path = Paths.get(pago.getRutaArchivo());
            if (!Files.exists(path)) return ResponseEntity.notFound().<Resource>build();

            Resource resource = new FileSystemResource(path);
            String tipo  = pago.getTipoArchivo() != null ? pago.getTipoArchivo() : "application/octet-stream";
            String nombre = pago.getNombreArchivo() != null ? pago.getNombreArchivo() : "comprobante";

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + nombre + "\"")
                .contentType(MediaType.parseMediaType(tipo))
                .<Resource>body(resource);

        }).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estudiante/resultados")
    public String verResultados(HttpSession session, Model model) {
        if (!tieneRol(session, "ESTUDIANTE")) return "redirect:/";
        String cedula = (String) session.getAttribute("cedula");
        Optional<Alumno> alumnoOpt = alumnoRepo.findByCedula(cedula);
        if (alumnoOpt.isEmpty()) return "redirect:/estudiante/dashboard";

        Alumno alumno = alumnoOpt.get();
        if (!alumno.isPagoAprobado()) {
            model.addAttribute("error", "Debes tener el pago aprobado para ver resultados.");
            return "estudiante/dashboard";
        }
        model.addAttribute("alumno",         alumno);
        model.addAttribute("resultadoTotal", resultadoTotalRepo.findByAlumno(alumno).orElse(null));

        if (resultadoTotalRepo.findByAlumno(alumno).isEmpty()) {
            model.addAttribute("mensajeEjemplo", "Aún no hay resultados cargados. Este es un ejemplo:");
            model.addAttribute("ejemploTotal",   crearEjemploResultadoTotal());
        }
        return "estudiante/resultados";
    }

    @GetMapping("/estudiante/resolucion")
    public String resolucionEstudiante(Model model) {
        model.addAttribute("rangos",  getRangosBeneficios());
        model.addAttribute("pdfLink", PDF_LINK);
        return "estudiante/resolucion_beneficios";
    }

    // ══════════════════════════════════════════════════════════════════════════
    // AUXILIARES
    // ══════════════════════════════════════════════════════════════════════════

    private static final String PDF_LINK =
        "https://www.uts.edu.co/sitio/wp-content/uploads/2019/10/ACUERDO-No.-01-009-CONSEJO-DIRECTIVO-Reglamento-de-Estimulos.pdf";

    private static final List<String> TIPOS_DOC = List.of("CC","TI","CE","PP","NIT");

    private boolean tieneRol(HttpSession session, String rolEsperado) {
        Object rol = session.getAttribute("rol");
        return rol != null && rol.toString().equals(rolEsperado);
    }

    private boolean isBlank(String s) { return s == null || s.isBlank(); }

    private List<Map<String, Object>> buildInforme() {
        List<Map<String, Object>> informe = new ArrayList<>();
        for (Alumno alumno : alumnoRepo.findAll()) {
            resultadoTotalRepo.findByAlumno(alumno).ifPresent(r -> {
                if (r.isPruebaNula()) return;
                Map<String, Object> item = new HashMap<>();
                item.put("nombre",    alumno.getNombre());
                item.put("cedula",    alumno.getCedula());
                item.put("facultad",  alumno.getFacultad());
                item.put("puntaje",   r.getPuntajeGlobal());
                item.put("beneficio", getBeneficioPorPuntaje(r.getPuntajeGlobal()));
                item.put("nivel",     r.getNivelDesempeno());
                informe.add(item);
            });
        }
        return informe;
    }

    private List<Map<String, String>> getRangosBeneficios() {
        List<Map<String, String>> rangos = new ArrayList<>();
        String[][] data = {
            {"Saber T&T","120–150 puntos","Se exime entrega del informe final o exonera Seminario de Grado II con 4.5"},
            {"Saber T&T","151–170 puntos","Se exime entrega del informe final o exonera Seminario de Grado II con 4.7. Beca 50% derechos de grado."},
            {"Saber T&T","171+ puntos",   "Se exime entrega del informe final o exonera Seminario de Grado II con 5.0. Beca 100% derechos de grado."},
            {"Saber Pro","180–210 puntos","Se exime entrega del informe final o exonera Seminario de Grado IV con 4.5"},
            {"Saber Pro","211–240 puntos","Se exime entrega del informe final o exonera Seminario de Grado IV con 4.7. Beca 50% derechos de grado."},
            {"Saber Pro","241+ puntos",   "Se exime entrega del informe final o exonera Seminario de Grado IV con 5.0. Beca 100% derechos de grado."},
        };
        for (String[] row : data) {
            Map<String, String> m = new HashMap<>();
            m.put("prueba", row[0]); m.put("rango", row[1]); m.put("beneficio", row[2]);
            rangos.add(m);
        }
        return rangos;
    }

    private String getBeneficioPorPuntaje(int p) {
        if (p >= 241) return "Exención informe + 100% beca derechos de grado (Saber Pro)";
        if (p >= 211) return "Exención informe + 50% beca derechos de grado (Saber Pro)";
        if (p >= 180) return "Exención informe o exoneración Seminario Grado IV con 4.5";
        if (p >= 171) return "Exención informe + 100% beca derechos de grado (Saber T&T)";
        if (p >= 151) return "Exención informe + 50% beca derechos de grado (Saber T&T)";
        if (p >= 120) return "Exención informe o exoneración Seminario Grado II con 4.5";
        return "Sin beneficio según reglamento actual";
    }

    private String getNivelPorPuntaje(int p) {
        if (p >= 191) return "Nivel 4";
        if (p >= 156) return "Nivel 3";
        if (p >= 126) return "Nivel 2";
        return "Nivel 1";
    }

    private String getNivelInglesIcfes(int p) {
        if (p >= 201) return "B+";
        if (p >= 176) return "B1";
        if (p >= 151) return "A2";
        if (p >= 126) return "A1";
        return "A-";
    }

    private ResultadoSaberPro crearEjemploResultadoTotal() {
        Alumno e = new Alumno();
        e.setPrimerNombre("Ejemplo"); e.setPrimerApellido("Estudiante");
        ResultadoSaberPro r = new ResultadoSaberPro(235, 85, "Nivel 4", 2026, e);
        r.setPuntajeComunicacionEscrita(178);        r.setNivelComunicacionEscrita("Nivel 3");
        r.setPuntajeRazonamientoCuantitativo(195);   r.setNivelRazonamientoCuantitativo("Nivel 4");
        r.setPuntajeLecturaCritica(182);             r.setNivelLecturaCritica("Nivel 3");
        r.setPuntajeCompetenciasCiudadanas(170);     r.setNivelCompetenciasCiudadanas("Nivel 3");
        r.setPuntajeIngles(185);                     r.setNivelIngles("B1"); r.setNivelInglesMarco("B1");
        r.setPuntajeFormulacionProyectos(210);       r.setNivelFormulacionProyectos("Nivel 4");
        r.setPuntajePensamientoCientifico(188);      r.setNivelPensamientoCientifico("Nivel 3");
        r.setPuntajeDisenioSoftware(202);            r.setNivelDisenioSoftware("Nivel 4");
        return r;
    }
}
