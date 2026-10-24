package com.mensagia.eme.resource;

import com.mensagia.eme.entity.MedicalQuestion;
import com.mensagia.eme.service.MedicalQuestionService;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.net.URI;
import java.util.Map;

@Path("/api/medical-questions")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MedicalQuestionResource {

    private static final Logger LOG = Logger.getLogger(MedicalQuestionResource.class);
    private final MedicalQuestionService service;

    public MedicalQuestionResource(MedicalQuestionService service) {
        this.service = service;
    }

    @GET
    public Response findAll() {
        return Response.ok(service.findAll()).build();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        return service.findById(id)
            .map(question -> Response.ok(question).build())
            .orElseGet(() -> notFound(id));
    }

    @POST
    public Response create(MedicalQuestion question) {
        try {
            MedicalQuestion created = service.create(question);
            return Response.created(URI.create("/api/medical-questions/" + created.id))
                .entity(created)
                .build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", e.getMessage()))
                .build();
        } catch (Exception e) {
            LOG.error("Error creating medical question", e);
            return Response.serverError()
                .entity(Map.of("error", "No se pudo crear la pregunta. Verifique que no exista otra pregunta con el mismo número y año de examen."))
                .build();
        }
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") Long id, MedicalQuestion question) {
        try {
            return service.update(id, question)
                .map(updated -> Response.ok(updated).build())
                .orElseGet(() -> notFound(id));
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(Map.of("error", e.getMessage()))
                .build();
        } catch (Exception e) {
            LOG.errorf(e, "Error updating medical question id=%d", id);
            return Response.serverError()
                .entity(Map.of("error", "No se pudo actualizar la pregunta. Verifique que no exista otra pregunta con el mismo número y año de examen."))
                .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        return service.delete(id)
            ? Response.noContent().build()
            : notFound(id);
    }

    private Response notFound(Long id) {
        return Response.status(Response.Status.NOT_FOUND)
            .entity(Map.of("error", "No existe medical question con id " + id))
            .build();
    }
}
