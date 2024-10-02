package com.hdmap.data.ibd.service;

import com.hdmap.data.ibd.dto.ErrorLog;
import com.hdmap.data.ibd.dto.TableMapping;
import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.filter.text.cql2.CQLException;
import org.locationtech.jts.io.ParseException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface TableParseService {

    List<ErrorLog> parse(TableMapping tableMapping, SimpleFeatureCollection sourceCollection, DataStore sourceDs, DataStore targetDs) throws IOException, CQLException, ParseException, FactoryException, TransformException, java.text.ParseException, ExecutionException, InterruptedException;

}
