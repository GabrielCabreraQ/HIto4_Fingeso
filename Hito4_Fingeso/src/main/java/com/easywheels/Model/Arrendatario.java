package com.easywheels.Model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@DiscriminatorValue("arrendatario")
public class Arrendatario extends Usuario{

    // Atributos
    @ElementCollection
    private List<String> tipoLicencia;
    @ElementCollection
    @CollectionTable(name = "arrendatario_documentos", joinColumns = @JoinColumn(name = "arrendatario_id_usuario"))
    private List<Documento> documentos;


    public Arrendatario() {
    }

    public Arrendatario(String rutUsuario, String nombreUsuario, String correoUsuario, String contraseniaUsuario, String telefonoUsuario, LocalDate fechaNacimiento, List<String> tipoLicencia, List<Documento> documentos) {
        super(rutUsuario, nombreUsuario, correoUsuario, contraseniaUsuario, telefonoUsuario, fechaNacimiento);
        this.tipoLicencia = tipoLicencia;
        this.documentos = documentos;
    }

    public Arrendatario(List<String> tipoLicencia, List<Documento> documentos) {
        this.tipoLicencia = tipoLicencia;
        this.documentos = documentos;
    }

    public List<String> getTipoLicencia() {
        return tipoLicencia;
    }

    public void setTipoLicencia(List<String> tipoLicencia) {
        this.tipoLicencia = tipoLicencia;
    }

    public List<Documento> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<Documento> documentos) {
        this.documentos = documentos;
    }
}