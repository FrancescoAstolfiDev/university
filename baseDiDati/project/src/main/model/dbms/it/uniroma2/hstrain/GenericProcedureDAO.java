package it.uniroma2.hstrain;

import java.sql.SQLException;

public interface GenericProcedureDAO<P> {

    P execute(Object... params) throws DAOException, SQLException;

}
