package br.net.bantads.clientews.model;

//import org.hibernate.annotations.Table;
//import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "tb_clientes")
public class Cliente implements Serializable, Comparable<Cliente> {
    private Object GenerationType;
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="id_cliente")
    private Integer id;
    @Column(name="nome_cliente")
    private String nome;
    @Column(name="email_cliente")
    private String email;
    @Column(name="cpf_cliente", unique = true)
    private String cpf;
    @Column(name="telefone_cliente")
    private String telefone;

    public Cliente(Object generationType) {
        super();
        GenerationType = generationType;
    }

    public Cliente(Object generationType, Integer id, String nome, String email, String cpf, String telefone) {
        super();
        GenerationType = generationType;
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.telefone = telefone;
    }

    public Cliente() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    @Override
    public int compareTo(Cliente cliente) {
        return getNome().compareTo(cliente.getNome());
    }

    public Object getGenerationType() {
        return GenerationType;
    }

    public void setGenerationType(Object generationType) {
        GenerationType = generationType;
    }
}
