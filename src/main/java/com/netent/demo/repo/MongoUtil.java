package com.netent.demo.repo;


import com.netent.demo.Exceptions.InSufficientQuantityException;
import com.netent.demo.Exceptions.NoEntryFoundException;
import com.netent.demo.Exceptions.QueryException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class MongoUtil {


  @Autowired
  private MongoTemplate mongoTemplate;


  public <T> T findOne(Query query, Class<T> entityClass) throws NoEntryFoundException {
    T object = mongoTemplate.findOne(query, entityClass);
    if (null != object) {
      return object;
    } else {
      throw new NoEntryFoundException();
    }
  }


  public <T> void insert(T entity) {
    try {
      mongoTemplate.save(entity);
    } catch (Exception e) {

    }
  }

  public <T> void findAndUpdate(Query query, Update update, Class<T> entityClass)
      throws InSufficientQuantityException {

    FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions();
    findAndModifyOptions.returnNew(true);
    if (mongoTemplate.findAndModify(query, update, findAndModifyOptions, entityClass) == null) {
      throw new InSufficientQuantityException();
    }
  }

  public <T> List<T> find(Query query, Class<T> entityClass) throws QueryException {
    try {
      List<T> entities = mongoTemplate.find(query, entityClass);
      return entities;
    } catch (Exception e) {
      throw new QueryException();
    }
  }

}
