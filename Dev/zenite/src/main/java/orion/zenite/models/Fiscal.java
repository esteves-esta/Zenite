package orion.zenite.models;

import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name="tblFiscal")
public class Fiscal  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idFiscal")
    private int id;

    @Column(name = "nomeFiscal", length = 100, nullable = false)
    private String nome;

    @CPF
    @Column(name = "cpf", length = 14, nullable = false, unique = true)
    private String cpf;

    @Column(name = "dtNasc", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "telefone", length = 11, nullable = false)
    private String numeroTelefone;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="fkEndereco")
    private Endereco endereco;

    @Column(length = 20, nullable = false, unique = true)
    private String registroFiscal;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="fkDispositivo")
    private Dispositivo dispositivo;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="fkConta")
    private Conta conta;

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getNumeroTelefone() {
        return numeroTelefone;
    }

    public void setNumeroTelefone(String numeroTelefone) {
        this.numeroTelefone = numeroTelefone;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getRegistroFiscal() {
        return registroFiscal;
    }

    public void setRegistroFiscal(String registroFiscal) {
        this.registroFiscal = registroFiscal;
    }

    public Dispositivo getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(Dispositivo dispositivo) {
        this.dispositivo = dispositivo;
    }
}
