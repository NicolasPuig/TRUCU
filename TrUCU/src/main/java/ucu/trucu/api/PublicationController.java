package ucu.trucu.api;

import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ucu.trucu.helper.PublicationHelper;
import ucu.trucu.model.dto.Image;
import ucu.trucu.model.dto.Offer;
import ucu.trucu.model.dto.Publication;
import ucu.trucu.model.dto.Report;
import ucu.trucu.util.log.Logger;
import ucu.trucu.util.log.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import ucu.trucu.database.querybuilder.Filter;
import ucu.trucu.model.filter.PublicationFilter;
import ucu.trucu.util.pagination.Page;

/**
 *
 * @author NicoPuig
 */
@RestController
@RequestMapping("trucu/publication")
@CrossOrigin(origins = "http://localhost:4200")
public class PublicationController {

    private static final Logger LOGGER = LoggerFactory.create(PublicationController.class);

    @Autowired
    private PublicationHelper publicationHelper;

    @PostMapping("/create")
    public ResponseEntity createPublication(@RequestBody Publication newPublication) {
        try {
            int idPublication = publicationHelper.createPublication(newPublication);
            LOGGER.info("Publicacion [idPublication=%s] creada correctamente", idPublication);
            return ResponseEntity.ok("Publicacion creada correctamente");
        } catch (SQLException ex) {
            LOGGER.error("Imposible crear publicacion -> %s", ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity updatePublication(@RequestParam int idPublication, @RequestBody Publication newValues) {
        try {
            publicationHelper.updatePublicationData(idPublication, newValues);
            LOGGER.info("Valores actualizados en publicacion [idPublication=%s]", idPublication);
            return ResponseEntity.ok("Valores de actualizados correctamente");
        } catch (SQLException ex) {
            LOGGER.error("Imposible actualizar valores para publicacion [idPublication=%s] -> %s", idPublication, ex);
            return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity cancelPublication(@RequestParam int idPublication) {
        try {
            publicationHelper.cancelPublication(idPublication);
            LOGGER.info("Valores actualizados en publicacion [idPublication=%s]", idPublication);
            return ResponseEntity.ok("Valores de actualizados correctamente");
        } catch (SQLException | IllegalStateException ex) {
            LOGGER.error("Imposible actualizar valores para publicacion [idPublication=%s] -> %s", idPublication, ex);
            return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteAccount(@RequestParam int idPublication) {
        try {
            if (publicationHelper.deletePublication(idPublication)) {
                LOGGER.info("Publicacion [idPublication=%s] eliminada correctamente", idPublication);
                return ResponseEntity.ok("Publicacion eliminada correctamente");
            } else {
                LOGGER.warn("Publicacion [idPublication=%s] no existente", idPublication);
                return ResponseEntity.ok("Publicacion no exsitente");
            }
        } catch (SQLException ex) {
            LOGGER.error("Imposible eliminar publicacion [idPublication=%s] -> %s", idPublication, ex);
            return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<Publication>> getPublications(
            PublicationFilter publicationFilter,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "0") int pageSize) {

        Filter filter = publicationFilter.toFilter();
        LOGGER.info("Obteniendo publicaciones filtradas por [%s]", filter);
        return ResponseEntity.ok(publicationHelper.getPublications(pageSize, pageNumber, filter));
    }

    @GetMapping("/images")
    public List<Image> getPublicationImages(@RequestParam int idPublication) {
        return publicationHelper.getPublicationImages(idPublication);
    }

    @GetMapping("/offers")
    public List<Offer> getPublicationOffers(@RequestParam int idPublication) {
        return publicationHelper.getPublicationOffers(idPublication);
    }

    @GetMapping("/reports")
    public List<Report> getPublicationReports(@RequestParam int idPublication) {
        return publicationHelper.getPublicationReports(idPublication);
    }
}
