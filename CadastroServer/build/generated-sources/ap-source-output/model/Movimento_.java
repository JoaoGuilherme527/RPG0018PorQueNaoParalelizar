package model;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import model.Pessoa;
import model.Produto;
import model.Usuario;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2025-06-10T22:53:01", comments="EclipseLink-2.7.12.v20230209-rNA")
@StaticMetamodel(Movimento.class)
public class Movimento_ { 

    public static volatile SingularAttribute<Movimento, Character> tipo;
    public static volatile SingularAttribute<Movimento, Integer> iDMovimento;
    public static volatile SingularAttribute<Movimento, Produto> iDProduto;
    public static volatile SingularAttribute<Movimento, String> quantidade;
    public static volatile SingularAttribute<Movimento, Usuario> iDUsuario;
    public static volatile SingularAttribute<Movimento, Pessoa> iDPessoa;
    public static volatile SingularAttribute<Movimento, String> valorUnitario;

}