package com.sap.dirigible.runtime.scripts;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sap.dirigible.repository.api.IRepository;
import com.sap.dirigible.repository.db.DBRepository;
import com.sap.dirigible.runtime.common.ICommonConstants;
import com.sap.dirigible.runtime.registry.DefinitionEnumerator;

public class DefinitionEnumeratorTest {

    private static final String REPOSITORY_JS_DEPLOY_PATH = "/db/dirigible/registry/public/" //$NON-NLS-1$
            + ICommonConstants.ARTIFACT_TYPE.SCRIPTING_SERVICES;

    private static final String FILE_ONE = REPOSITORY_JS_DEPLOY_PATH + "/test.jstest";

    private static final String FILE_TWO = REPOSITORY_JS_DEPLOY_PATH + "/test/test1.jstest";

    private static final String FILE_THREE = REPOSITORY_JS_DEPLOY_PATH + "/test/test2.html";

    private static IRepository repository;

    @Before
    public void setUp() {
        final DataSource dataSource = createLocal();
        try {

            repository = new DBRepository(dataSource, "guest", false);
            repository.createResource(FILE_ONE, ("test1").getBytes());
            repository.createResource(FILE_TWO, ("test2").getBytes());
            repository.createResource(FILE_THREE, ("text_html").getBytes());

        } catch (final Exception e) {
            assertTrue(e.getMessage(), false);
            e.printStackTrace();
        }
    }

    @After
    public void cleanup() {
        try {

            repository.removeResource(FILE_ONE);
            repository.removeResource(FILE_TWO);
            repository.removeResource(FILE_THREE);

        } catch (final Exception e) {
            assertTrue(e.getMessage(), false);
            e.printStackTrace();
        }
    }

    @Test
    public void testEnumerator() {
        try {
            final DefinitionEnumerator defEnumerator = new DefinitionEnumerator(REPOSITORY_JS_DEPLOY_PATH, repository.getCollection(REPOSITORY_JS_DEPLOY_PATH), ".jstest");
            final List<String> list = defEnumerator.toArrayList();
            assertNotNull(defEnumerator);
            assertTrue(list.size() == 2);
            assertTrue(list.get(0).equals("/test.jstest"));
            assertTrue(list.get(1).equals("/test/test1.jstest"));

        } catch (final IOException e) {
            assertTrue(e.getMessage(), false);
            e.printStackTrace();
        }
    }

    public static DataSource createLocal() {
        final EmbeddedDataSource dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName("derby"); //$NON-NLS-1$
        dataSource.setCreateDatabase("create"); //$NON-NLS-1$
        return dataSource;
    }

}
