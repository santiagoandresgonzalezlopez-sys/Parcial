package com.parcial.app.config;

import com.parcial.app.model.*;
import com.parcial.app.model.Usuario.Rol;
import com.parcial.app.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * Crea datos iniciales al arrancar la aplicación (solo si no existen).
 *
 * Usuarios por defecto:
 *   ADMIN       → admin@sistema.edu.co        / admin123
 *   COORDINADOR → coordinador@sistema.edu.co  / coord123
 *
 * Estudiantes: 35 registros del informe Saber Pro 2026.
 *   Acceso de cada estudiante: [cedula]@estudiante.edu.co / [cedula]
 */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatos(UsuarioRepository    usuarioRepo,
                                AlumnoRepository     alumnoRepo,
                                ResultadoSaberProRepository resultadoRepo,
                                FacultadRepository   facultadRepo) {
        return args -> {

            // ── Usuarios administrativos ───────────────────────────────────────
            crearUsuarioSiNoExiste(usuarioRepo,
                "admin@sistema.edu.co", "admin123", Rol.ADMIN, "00000001", "Administrador");
            crearUsuarioSiNoExiste(usuarioRepo,
                "coordinador@sistema.edu.co", "coord123", Rol.COORDINADOR, "00000002", "Coordinador");

            // ── Facultad por defecto ───────────────────────────────────────────
            if (facultadRepo.count() == 0) {
                facultadRepo.save(new Facultad(
                    "Facultad de Ingeniería",
                    "Director General",
                    "Facultad de Ingeniería de Sistemas y afines"
                ));
            }

            // ── Estudiantes del Excel ──────────────────────────────────────────
            if (alumnoRepo.count() == 0) {
                cargarEstudiantes(alumnoRepo, usuarioRepo, resultadoRepo);
            }
        };
    }

    // ──────────────────────────────────────────────────────────────────────────

    private void crearUsuarioSiNoExiste(UsuarioRepository repo,
                                        String email, String pass,
                                        Rol rol, String cedula, String nombre) {
        if (!repo.existsByEmail(email)) {
            repo.save(new Usuario(email, pass, rol, cedula, nombre));
            System.out.println("✔ Usuario creado: " + email);
        }
    }

    private void cargarEstudiantes(AlumnoRepository alumnoRepo,
                                   UsuarioRepository usuarioRepo,
                                   ResultadoSaberProRepository resultadoRepo) {

        // Cada fila: tipoDoc, cedula, pApellido, sApellido, pNombre, sNombre,
        //            correo, tel, registro,
        //            pGlobal, nivelGlobal,
        //            pCE, nCE, pRQ, nRQ, pLC, nLC, pCC, nCC,
        //            pIng, nIngIcfes, nIngMarco,
        //            pFP, nFP, pPC, nPC, pDS, nDS,
        //            anulado
        Object[][] data = {
            {"CC","1111111111","BARBOSA","QUINTERO","KATERINE","","KATERINE@gmail.com","3165258525","EK20183007722",
             200,"Nivel 4", 128,"Nivel 2",182,"Nivel 3",202,"Nivel 4",206,"Nivel 4",
             183,"B1","B1", 185,"Nivel 3",160,"Nivel 3",197,"Nivel 4", false},

            {"CC","2222222222","QUINTERO","PARRA","CLAUDIA","","CLAUDIA@gmail.com","3159686985","EK20183140703",
             165,"Nivel 3", 125,"Nivel 1",151,"Nivel 2",179,"Nivel 3",163,"Nivel 3",
             205,"B1","B2", 182,"Nivel 3",144,"Nivel 2",136,"Nivel 2", false},

            {"CC","3333333333","PARRA","BARBOSA","YAMILE","","YAMILE@gmail.com","3025252525","EK20183040545",
             164,"Nivel 3", 159,"Nivel 3",172,"Nivel 3",182,"Nivel 3",142,"Nivel 2",
             165,"A2","A2", 167,"Nivel 3",132,"Nivel 2",148,"Nivel 2", false},

            {"CC","4444444444","ANAYA","ANAYA","LEIDY","","LEIDY@gmail.com","3147474747","EK20183025381",
             160,"Nivel 3", 146,"Nivel 2",199,"Nivel 4",157,"Nivel 3",149,"Nivel 2",
             147,"A2","A2", 174,"Nivel 3",127,"Nivel 2",171,"Nivel 3", false},

            {"CC","5555555555","FLOR","GARCIA","GIORDAN","","GIORDAN@gmail.com","3165656565","EK20183025335",
             160,"Nivel 3", 198,"Nivel 4",153,"Nivel 2",147,"Nivel 2",157,"Nivel 3",
             146,"A2","A2", 168,"Nivel 3",114,"Nivel 1",160,"Nivel 3", false},

            {"CC","6666666666","GARCIA","MANOSALVA","JULIANA","","JULIANA@gmail.com","3100000000","EK20183122648",
             157,"Nivel 3", 179,"Nivel 3",172,"Nivel 3",158,"Nivel 3",140,"Nivel 2",
             136,"A1","A1", 128,"Nivel 2",121,"Nivel 1",142,"Nivel 2", false},

            {"CC","7777777777","MANOSALVA","MENDOZA","LEONEL","","LEONEL@gmail.com","3012121212","EK20183064605",
             153,"Nivel 2", 115,"Nivel 1",152,"Nivel 2",159,"Nivel 3",172,"Nivel 3",
             165,"A2","A2", 142,"Nivel 2",118,"Nivel 1",119,"Nivel 1", false},

            {"CC","8888888888","MENDOZA","BELTRAN","CRISTIAN","","CRISTIAN@gmail.com","3125252525","EK20183187351",
             151,"Nivel 2", 132,"Nivel 2",123,"Nivel 1",125,"Nivel 1",169,"Nivel 3",
             204,"B1","B2", 173,"Nivel 3",127,"Nivel 2",171,"Nivel 3", false},

            {"CC","9999999999","BELTRAN","SANCHEZ","KYLIAM","","KYLIAM@gmail.com","3114141414","EK20183233820",
             150,"Nivel 2", 86,"Nivel 1",187,"Nivel 3",160,"Nivel 3",171,"Nivel 3",
             148,"A2","A2", 162,"Nivel 3",125,"Nivel 1",142,"Nivel 2", false},

            {"CC","1010101010","SANTAMARIA","GONZALEZ","SANTIAGO","ANDRES","SANTIAGO@gmail.com","3159595959","EK20183030016",
             150,"Nivel 2", 175,"Nivel 3",149,"Nivel 2",145,"Nivel 2",158,"Nivel 3",
             125,"A1","A1", 162,"Nivel 3",76,"Nivel 1",125,"Nivel 1", false},

            {"CC","1234567890","SANCHEZ","LOPEZ","ANDRES","FELIPE","ANDRES@gmail.com","3026262626","EK20183047073",
             149,"Nivel 2", 209,"Nivel 4",143,"Nivel 2",117,"Nivel 1",129,"Nivel 2",
             147,"A2","A2", 137,"Nivel 2",125,"Nivel 1",136,"Nivel 2", false},

            {"CC","9874563210","ROMERO","CARDENAS","DILAN","ANDREY","DILAN@gmail.com","3247474848","EK20183236451",
             146,"Nivel 2", 93,"Nivel 1",183,"Nivel 3",155,"Nivel 2",164,"Nivel 3",
             133,"A1","A1", 174,"Nivel 3",130,"Nivel 2",154,"Nivel 2", false},

            {"CC","4569871230","LUNA","GOMEZ","ANDREY","SANTIAGO","ANDREY@gmail.com","3185868685","EK20183041714",
             141,"Nivel 2", 125,"Nivel 1",157,"Nivel 3",138,"Nivel 2",135,"Nivel 2",
             152,"A2","A2", 176,"Nivel 3",128,"Nivel 2",165,"Nivel 3", false},

            {"CC","1598753214","TRIANA","PEREZ","MARIA","ALEJANDRA","MARIA@gmail.com","3125828258","EK20183187801",
             141,"Nivel 2", 150,"Nivel 2",136,"Nivel 2",145,"Nivel 2",150,"Nivel 2",
             126,"A1","A1", 148,"Nivel 2",129,"Nivel 2",131,"Nivel 2", false},

            {"CC","4563211236","SUAREZ","LOPEZ","JHON","EDINSON","JHON@gmail.com","3069696969","EK20183176566",
             140,"Nivel 2", 128,"Nivel 2",146,"Nivel 2",146,"Nivel 2",132,"Nivel 2",
             147,"A2","A2", 130,"Nivel 2",110,"Nivel 1",125,"Nivel 1", false},

            {"CC","1002233665","GARCIA","CASTILLO","JOSE","MANUEL","JOSE@gmail.com","3038500380","EK20183204427",
             139,"Nivel 2", 129,"Nivel 2",138,"Nivel 2",148,"Nivel 2",146,"Nivel 2",
             135,"A1","A1", 109,"Nivel 1",107,"Nivel 1",131,"Nivel 2", false},

            {"CC","9966554411","PINZON","MARTINEZ","EMANUEL","ANTONIO","EMANUEL@gmail.com","3146945753","EK20183196280",
             138,"Nivel 2", 153,"Nivel 2",123,"Nivel 1",127,"Nivel 2",147,"Nivel 2",
             140,"A1","A1", 145,"Nivel 2",143,"Nivel 2",160,"Nivel 3", false},

            {"CC","1558877441","JAIMES","PEREZ","JUAN","DANIEL","JUAN@gmail.com","3159622109","EK20183173799",
             137,"Nivel 2", 166,"Nivel 3",157,"Nivel 3",124,"Nivel 1",100,"Nivel 1",
             140,"A1","A1", 100,"Nivel 1",105,"Nivel 1",113,"Nivel 1", false},

            {"CC","2365998877","NIÑO","BAUTISTA","FELIPE","ANDRES","FELIPE@gmail.com","3185593275","EK20183009565",
             134,"Nivel 2", 165,"Nivel 3",137,"Nivel 2",136,"Nivel 2",118,"Nivel 1",
             116,"A-","A0", 146,"Nivel 2",122,"Nivel 1",154,"Nivel 2", false},

            {"CC","1236547896","FABIAN","ORDUZ","DIEGO","ALBERTO","DIEGO@gmail.com","3040217338","EK20183117756",
             133,"Nivel 2", 139,"Nivel 2",93,"Nivel 1",168,"Nivel 3",150,"Nivel 2",
             114,"A-","A0", 102,"Nivel 1",123,"Nivel 1",94,"Nivel 1", false},

            {"CC","1234567852","HERNANDEZ","TARAZONA","NICOLAS","ANDRES","NICOLAS@gmail.com","3152986165","EK20183044579",
             132,"Nivel 2", 116,"Nivel 1",166,"Nivel 3",136,"Nivel 2",104,"Nivel 1",
             140,"A1","A1", 158,"Nivel 3",125,"Nivel 1",154,"Nivel 2", false},

            {"CC","1200445500","LARIOS","PLATA","JOHAN","MIGUEL","JOHAN@gmail.com","3015463745","EK20183045760",
             131,"Nivel 2", 149,"Nivel 2",123,"Nivel 1",129,"Nivel 2",121,"Nivel 1",
             131,"A1","A1", 101,"Nivel 1",102,"Nivel 1",165,"Nivel 3", false},

            {"CC","1747474747","CALDERON","MALAVER","GUSTAVO","","GUSTAVO@gmail.com","3056353022","EK20183034044",
             130,"Nivel 2", 127,"Nivel 2",147,"Nivel 2",134,"Nivel 2",111,"Nivel 1",
             131,"A1","A1", 65,"Nivel 1",112,"Nivel 1",94,"Nivel 1", false},

            {"CC","4156989895","VILLARREAL","OLAVE","CRISTINA","","CRISTINA@gmail.com","3176278278","EK20183041521",
             129,"Nivel 2", 96,"Nivel 1",162,"Nivel 3",114,"Nivel 1",131,"Nivel 2",
             144,"A1","A1", 122,"Nivel 1",112,"Nivel 1",131,"Nivel 2", false},

            {"CC","4795478569","RESTREPO","MARIN","ALEJANDRA","DANIELA","ALEJANDRA@gmail.com","3140470013","EK20183027436",
             126,"Nivel 2", 81,"Nivel 1",134,"Nivel 2",126,"Nivel 2",149,"Nivel 2",
             139,"A1","A1", 127,"Nivel 2",136,"Nivel 2",142,"Nivel 2", false},

            {"CC","4587459658","CACERES","URIBE","DEYNER","","DEYNER@gmail.com","3068940804","EK20183031592",
             125,"Nivel 1", 124,"Nivel 1",135,"Nivel 2",108,"Nivel 1",92,"Nivel 1",
             165,"A2","A2", 132,"Nivel 2",104,"Nivel 1",131,"Nivel 2", false},

            {"CC","4123654785","TABARES","ARCHILA","ARTURO","","ARTURO@gmail.com","3134886567","EK20183004153",
             124,"Nivel 1", 131,"Nivel 2",131,"Nivel 2",107,"Nivel 1",88,"Nivel 1",
             162,"A2","A2", 136,"Nivel 2",112,"Nivel 1",148,"Nivel 2", false},

            {"CC","4569874123","NARANJO","DELGADO","HILDA","","HILDA@gmail.com","3114015729","EK20183030783",
             122,"Nivel 1", 166,"Nivel 3",113,"Nivel 1",113,"Nivel 1",112,"Nivel 1",
             106,"A-","A0", 135,"Nivel 2",117,"Nivel 1",119,"Nivel 1", false},

            {"CC","1236552100","PRADA","ANAYA","JULIAN","ANDRES","JULIAN@gmail.com","3167553867","EK20183024754",
             122,"Nivel 1", 119,"Nivel 1",125,"Nivel 1",137,"Nivel 2",107,"Nivel 1",
             123,"A1","A1", 83,"Nivel 1",104,"Nivel 1",119,"Nivel 1", false},

            {"CC","1896547856","VARGAS","ACOSTA","DIANA","CECILIA","DIANA@gmail.com","3188363811","EK20183186200",
             114,"Nivel 1", 95,"Nivel 1",120,"Nivel 1",151,"Nivel 2",86,"Nivel 1",
             119,"A-","A0", 149,"Nivel 2",103,"Nivel 1",119,"Nivel 1", false},

            {"CC","4152365895","TORRES","SAAVEDRA","DANIELA","ALEJANDRA","DANIELA@gmail.com","3010260892","EK20183182410",
             113,"Nivel 1", 109,"Nivel 1",105,"Nivel 1",104,"Nivel 1",103,"Nivel 1",
             142,"A1","A1", 102,"Nivel 1",135,"Nivel 2",80,"Nivel 1", false},

            {"CC","4152635256","ORTIZ","CALDERON","DUBAN","","DUBAN@gmail.com","3198489895","EK20183213735",
             107,"Nivel 1", 128,"Nivel 2",81,"Nivel 1",107,"Nivel 1",102,"Nivel 1",
             119,"A-","A0", 130,"Nivel 2",111,"Nivel 1",125,"Nivel 1", false},

            {"CC","4152142254","VILLAMIZAR","ORTIZ","ARLEY","","ARLEY@gmail.com","3042490457","EK20183065220",
             106,"Nivel 1", 134,"Nivel 2",96,"Nivel 1",92,"Nivel 1",110,"Nivel 1",
             97,"A-","A0", 83,"Nivel 1",107,"Nivel 1",119,"Nivel 1", false},

            {"CC","1154849846","RESTREPO","JAIMES","PEDRO","","PEDRO@gmail.com","3112516276","EK20183028123",
             96,"Nivel 1", 0,"Nivel 1",117,"Nivel 1",122,"Nivel 1",105,"Nivel 1",
             137,"A1","A1", 157,"Nivel 3",96,"Nivel 1",131,"Nivel 2", false},

            // Pruebas ANULADAS
            {"CC","4564845644","HIGUERA","GARCIA","GABRIEL","ANTONIO","GABRIEL@gmail.com","3099622418","EK20183207870",
             0,"ANULADO", 0,"",0,"",0,"",0,"",
             0,"","", 0,"",0,"",0,"", true},

            {"CC","4564845600","MATIZ","SUAREZ","KEVIN","ARLEY","KEVIN@gmail.com","3174815335","EK20183144329",
             0,"ANULADO", 0,"",0,"",0,"",0,"",
             0,"","", 0,"",0,"",0,"", true},
        };

        for (Object[] row : data) {
            String cedula = (String) row[1];

            // Evitar duplicados en re-ejecuciones
            if (alumnoRepo.existsByCedula(cedula)) continue;

            // ── Crear alumno ──────────────────────────────────────────────────
            Alumno a = new Alumno();
            a.setTipoDocumento((String) row[0]);
            a.setCedula(cedula);
            a.setPrimerApellido((String) row[2]);
            a.setSegundoApellido((String) row[3]);
            a.setPrimerNombre((String) row[4]);
            String sNombre = (String) row[5];
            a.setSegundoNombre(sNombre.isBlank() ? null : sNombre);
            a.setCorreoElectronico((String) row[6]);
            a.setTelefono((String) row[7]);
            a.setNumeroRegistro((String) row[8]);
            a.setFacultad("Facultad de Ingeniería");
            a.setPrograma("Ingeniería de Sistemas");
            a.setPagoAprobado(true);   // todos con pago aprobado para demostración
            alumnoRepo.save(a);

            // ── Crear usuario de acceso ───────────────────────────────────────
            String emailAcceso = cedula + "@estudiante.edu.co";
            if (!usuarioRepo.existsByEmail(emailAcceso)) {
                usuarioRepo.save(new Usuario(emailAcceso, cedula,
                    Rol.ESTUDIANTE, cedula, a.getNombre()));
            }

            // ── Crear resultado ───────────────────────────────────────────────
            boolean anulado = (boolean) row[28];

            ResultadoSaberPro r = new ResultadoSaberPro();
            r.setAlumno(a);
            r.setAnioPresentacion(2026);
            r.setPruebaNula(anulado);

            if (!anulado) {
                r.setPuntajeGlobal      ((int)    row[9]);
                r.setNivelDesempeno     ((String) row[10]);
                r.setPuntajeComunicacionEscrita       ((int)    row[11]);
                r.setNivelComunicacionEscrita         ((String) row[12]);
                r.setPuntajeRazonamientoCuantitativo  ((int)    row[13]);
                r.setNivelRazonamientoCuantitativo    ((String) row[14]);
                r.setPuntajeLecturaCritica            ((int)    row[15]);
                r.setNivelLecturaCritica              ((String) row[16]);
                r.setPuntajeCompetenciasCiudadanas    ((int)    row[17]);
                r.setNivelCompetenciasCiudadanas      ((String) row[18]);
                r.setPuntajeIngles                    ((int)    row[19]);
                r.setNivelIngles                      ((String) row[20]);
                r.setNivelInglesMarco                 ((String) row[21]);
                r.setPuntajeFormulacionProyectos      ((int)    row[22]);
                r.setNivelFormulacionProyectos        ((String) row[23]);
                r.setPuntajePensamientoCientifico     ((int)    row[24]);
                r.setNivelPensamientoCientifico       ((String) row[25]);
                r.setPuntajeDisenioSoftware           ((int)    row[26]);
                r.setNivelDisenioSoftware             ((String) row[27]);
            }

            resultadoRepo.save(r);
        }

        System.out.println("✔ Estudiantes del informe Saber Pro 2026 cargados.");
    }
}
