package com.carpenter.core.control.service.login;

import org.apache.commons.codec.digest.DigestUtils;
import org.jboss.security.PicketBoxMessages;
import org.jboss.security.auth.spi.DatabaseServerLoginModule;
import org.mindrot.jbcrypt.BCrypt;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.login.LoginException;
import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import java.security.Principal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExtendedDatabaseServerLoginModule extends DatabaseServerLoginModule {

    protected static final String EMPLOYER_QUERY = "SELECT EMPLOYEES.FIRST_NAME, EMPLOYEES.LAST_NAME FROM EMPLOYEES WHERE EMPLOYEES.EMAIL=?";

    protected static final String UPDATE_HASHED_PWD_QUERY = "UPDATE EMPLOYEES SET EMPLOYEES.PASSWORD =? WHERE EMPLOYEES.EMAIL=?";

    protected ExtendedPrincipal extendedPrincipal;

    @Override
    public boolean login() throws LoginException {
        boolean login = super.login();
        String email = getUsername();
        if (login) {
            extendedPrincipal = fetchExtendedPrincipal(email);
        }
        return login;
    }

    @Override
    protected Principal getIdentity() {
        return extendedPrincipal != null ? extendedPrincipal : super.getIdentity();
    }

    @Override
    protected boolean validatePassword(String enteredPassword, String encrypted) {
        if (enteredPassword == null || encrypted == null) {
            return false;
        } else if (encrypted.startsWith("$")) {
            return BCrypt.checkpw(enteredPassword, encrypted);
        } else {
            boolean isValid = checkSHAPwd(enteredPassword, encrypted);
            if (isValid) {
                String bcryptHashedPwd = BCrypt.hashpw(enteredPassword, BCrypt.gensalt());
                try {
                    updateStaffMemberPasswordHash(bcryptHashedPwd);
                } catch (LoginException e) {
                    log.error("Error changing users password to BCrypt hashed.", e);
                }
            }
            return isValid;
        }
    }

    private boolean checkSHAPwd(String enteredPassword, String encrypted) {
        String hashedPwd = DigestUtils.sha256Hex(enteredPassword);
        return hashedPwd.equalsIgnoreCase(encrypted);
    }

    private ExtendedPrincipal fetchExtendedPrincipal(String email) throws LoginException {
        ExtendedPrincipal result = new ExtendedPrincipal();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Transaction tx = null;
        tx = getTransaction(tx);

        try {
            LoginException le;
            try {
                InitialContext ctx = new InitialContext();
                DataSource ds = (DataSource) ctx.lookup(this.dsJndiName);
                conn = ds.getConnection();
                ps = conn.prepareStatement(EMPLOYER_QUERY);
                ps.setString(1, email);
                rs = ps.executeQuery();
                if (!rs.next()) {
                    throw PicketBoxMessages.MESSAGES.noMatchingUsernameFoundInPrincipals();
                }
                String firstName = rs.getNString(1);
                String lastName = rs.getString(2);
                result.setEmail(email);
                result.setFirstName(firstName);
                result.setLastName(lastName);
            } catch (NamingException var28) {
                le = new LoginException(PicketBoxMessages.MESSAGES.failedToLookupDataSourceMessage(this.dsJndiName));
                le.initCause(var28);
                throw le;
            } catch (SQLException var29) {
                le = new LoginException(PicketBoxMessages.MESSAGES.failedToProcessQueryMessage());
                le.initCause(var29);
                throw le;
            }
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException var26) {
                }
            }
            closeConnectionsAndResumeSuspension(conn, ps, tx);
        }
        return result;
    }

    private void updateStaffMemberPasswordHash(String bcryptHashedPwd) throws LoginException {
        Connection conn = null;
        PreparedStatement ps = null;
        Transaction tx = null;
        tx = getTransaction(tx);

        try {
            LoginException le;
            try {
                InitialContext ctx = new InitialContext();
                DataSource ds = (DataSource) ctx.lookup(this.dsJndiName);
                conn = ds.getConnection();
                ps = conn.prepareStatement(UPDATE_HASHED_PWD_QUERY);
                ps.setString(1, bcryptHashedPwd);
                ps.setString(2, getUsername());
                ps.executeUpdate();
            } catch (NamingException var28) {
                le = new LoginException(PicketBoxMessages.MESSAGES.failedToLookupDataSourceMessage(this.dsJndiName));
                le.initCause(var28);
                throw le;
            } catch (SQLException var29) {
                le = new LoginException(PicketBoxMessages.MESSAGES.failedToProcessQueryMessage());
                le.initCause(var29);
                throw le;
            }
        } finally {
            closeConnectionsAndResumeSuspension(conn, ps, tx);
        }
    }

    private Transaction getTransaction(Transaction tx) {
        if (this.suspendResume) {
            try {
                if (this.tm == null) {
                    throw PicketBoxMessages.MESSAGES.invalidNullTransactionManager();
                }
                tx = this.tm.suspend();
            } catch (SystemException var27) {
                throw new RuntimeException(var27);
            }
        }
        return tx;
    }

    private void closeConnectionsAndResumeSuspension(Connection conn, PreparedStatement ps, Transaction tx) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException var25) {
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException var24) {
            }
        }
        if (this.suspendResume) {
            try {
                this.tm.resume(tx);
            } catch (Exception var23) {
                throw new RuntimeException(var23);
            }
        }
    }

}
