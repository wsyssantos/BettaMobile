package br.com.ftt.betta.mobile.dao;

import java.util.List;

import br.com.ftt.betta.moblie.util.StringUtil;

public class Filme
{
    private int          id;
    private String       nome;
    private String       urlImagem;
    private String       urlFilme;
    private String       duracao;
    private String       diretores;
    private String       atores;
    private String       sinopse;
    private int          ano;
    private int          avaliacao;
    private List<Idioma> idiomas;

    public int getId( )
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public String getNome( )
    {
        return nome;
    }

    public void setNome( String nome )
    {
        this.nome = nome;
    }

    public String getUrlImagem( )
    {
        return urlImagem;
    }

    public void setUrlImagem( String urlImagem )
    {
        this.urlImagem = urlImagem;
    }

    public String getUrlFilme( )
    {
        return urlFilme;
    }

    public void setUrlFilme( String urlFilme )
    {
        this.urlFilme = urlFilme;
    }

    public String getDuracao( )
    {
        return duracao;
    }

    public void setDuracao( String duracao )
    {
        this.duracao = duracao;
    }

    public String getDiretores( )
    {
        return diretores;
    }

    public void setDiretores( String diretores )
    {
        this.diretores = diretores;
    }

    public String getAtores( )
    {
        return atores;
    }

    public void setAtores( String atores )
    {
        this.atores = atores;
    }

    public String getSinopse( )
    {
        return sinopse;
    }

    public void setSinopse( String sinopse )
    {
        this.sinopse = sinopse;
    }

    public int getAno( )
    {
        return ano;
    }

    public void setAno( int ano )
    {
        this.ano = ano;
    }

    public int getAvaliacao( )
    {
        return avaliacao;
    }

    public void setAvaliacao( int avaliacao )
    {
        this.avaliacao = avaliacao;
    }

    public List<Idioma> getIdiomas( )
    {
        return idiomas;
    }

    public void setIdiomas( List<Idioma> idiomas )
    {
        this.idiomas = idiomas;
    }

    public String getStrDuracao( )
    {
        return StringUtil.formatIntoHHMMSS( Integer.valueOf( duracao ) ) ;
    }
}
