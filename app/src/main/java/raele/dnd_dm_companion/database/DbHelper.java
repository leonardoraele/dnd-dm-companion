package raele.dnd_dm_companion.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import raele.util.android.log.Log;

/**
 * Created by lpr on 21/09/15.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String SETUP_SCRIPT_FILENAME = "database_setup.sql";
    private static final String DATABASE_FILENAME = "database.db";
    private static final int DATABASE_VERSION = 13;
    private static final String[] XML_DATA_FILES = new String[] {
            "_size.xml",
            "_super_race.xml",
            "_sub_race.xml",
            "_source.xml",
            "_feature.xml",
            "_racial_trait.xml",
            "_name_type.xml",
            "_sample_name.xml",
    };

    private final Context mContext;

    public DbHelper(Context context) {
        super(context, DATABASE_FILENAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.begin();

        Log.info("Loading database setup sql file...");
        String[] sqlStatements;
        try {
            InputStream input = mContext.getAssets().open(SETUP_SCRIPT_FILENAME);
            StringWriter writer = new StringWriter();
            IOUtils.copy(input, writer);
            sqlStatements = writer.toString().split(";");
        } catch (IOException e) {
            Log.printStackTrace(e);
            return;
        }

        Log.info("Setting up database tables...");
        for (int i = 0; i < sqlStatements.length; i++) {
            try {
                db.execSQL(sqlStatements[i]);
            } catch (SQLiteException e) {
                if (!e.getMessage().contains("not an error (code 0)")) {
                    throw e;
                }
            }
        }

        Log.info("Filling up tables with xml data...");
        fillDatabase(db);

        Log.info("SQL setup file '" + SETUP_SCRIPT_FILENAME + "' was executed.");
        Log.end();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.begin();
        Log.info("Updating database from version " + oldVersion + " to " + newVersion);

        Log.info("(TODO) Calling the initial database setup as this is a stub, not implemented, method.");
        onCreate(db); // TODO

        Log.end();
    }

    private void fillDatabase(SQLiteDatabase db) {
        Log.begin();
        HashMap<String, Integer> idTable = new HashMap<>();
        int id = 1;

        for (int i = 0; i < XML_DATA_FILES.length; i++) {
            Log.info("Inserting data to " + XML_DATA_FILES[i] + " table...");
            id = readXmlData(db, XML_DATA_FILES[i], idTable, id);
        }

        Log.end();
    }

    private int readXmlData(SQLiteDatabase db, String assetName, Map<String, Integer> idMap, int nextId) {
        InputStream input;
        try {
            input = mContext.getAssets().open(assetName);
        } catch (IOException e) {
            Log.printStackTrace(e);
            throw new RuntimeException(e);
        }

        Document doc;
        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            Log.printStackTrace(e);
            throw new RuntimeException(e);
        }

        doc.getDocumentElement().normalize();

        NodeList inserts = doc.getElementsByTagName("insert");

        db.beginTransaction();

        for (int i = 0; i < inserts.getLength(); i++) {
            Node insertNode = inserts.item(i);
            String tableName = insertNode.getAttributes().getNamedItem("table").getTextContent().trim();
            ContentValues values = new ContentValues();
            NodeList columnNodes = insertNode.getChildNodes();

            for (int j = 0; j < columnNodes.getLength(); j++) {
                if (columnNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                    Node columnNode = columnNodes.item(j);
                    NamedNodeMap attrs = columnNode.getAttributes();
                    String columnName = attrs.getNamedItem("name").getTextContent().trim();
                    String value = columnNode.getTextContent().trim();

                    if (attrs.getNamedItem("id") != null) {
                        String fakeId = columnNode.getTextContent().trim();
                        Integer realId = idMap.get(fakeId);
                        if (realId == null) {
                            realId = nextId++;
                            idMap.put(fakeId, realId);
                        }

                        values.put(columnName, realId);
                    } else if (attrs.getNamedItem("translation") != null) {
                        String fakeId = "_translation" + tableName + columnName + "_" + i;
                        Integer realId = idMap.get(fakeId);
                        if (realId == null) {
                            realId = nextId++;
                            idMap.put(fakeId, realId);
                        }

                        String language = attrs.getNamedItem("translation").getTextContent().trim();
                        ContentValues translationValues = new ContentValues();
                        translationValues.put("_id", realId);
                        translationValues.put("_language", language);
                        translationValues.put("_text", value);
                        db.insert("_translation", null, translationValues);

                        values.put(columnName, realId);
                    } else {
                        values.put(columnName, value);
                    }
                }
            }

            db.insert(tableName, null, values);
        }

        db.setTransactionSuccessful();
        db.endTransaction();

        return nextId;
    }

}
