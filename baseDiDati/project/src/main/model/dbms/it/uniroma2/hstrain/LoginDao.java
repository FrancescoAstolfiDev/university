package it.uniroma2.hstrain;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class LoginDao implements GenericProcedureDAO<CredentialsBean>  {
    @Override
    public CredentialsBean execute(Object... params) throws DAOException, SQLException {
        String username=(String)params[0];
        String password=(String)params[1];
        int role;
        try{
            Connection conn=ConnectionFactoryFromFile.getConnection();
            CallableStatement cs=conn.prepareCall("{call login(?,?,?)}");
            cs.setString(1,username);
            cs.setString(2,password);
            cs.registerOutParameter(3, Types.NUMERIC);
            cs.executeQuery();
            role=cs.getInt(3);
        }catch(SQLException e) {
            throw new DAOException("Login error: " + e.getMessage());
        }
        return new CredentialsBean(username, password, Role.fromInt(role));
    }
    public void signup(SignupBean bean) throws SQLException {
        Connection conn = ConnectionFactoryFromFile.getConnection();
        CallableStatement cs = conn.prepareCall("{call crea_acquirente(?,?,?,?,?,?)}");
        cs.setString(1, bean.getPassword());
        cs.setString(2, bean.getIdUsr());
        cs.setString(3, bean.getNome());
        cs.setString(4, bean.getCognome());
        cs.setString(5, bean.getDateBorn());
        cs.setString(6, bean.getCreditCard_Role());
        cs.executeQuery();
    }

}
