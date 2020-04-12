package com.netent.demo.service;

import com.netent.demo.Exceptions.NoMediaPostMatchWithIsbnException;
import com.netent.demo.Exceptions.NoSuchBookFoundException;
import com.netent.demo.Exceptions.UnableToProcessQueryException;
import java.util.List;

public interface MediaServiceInterface {

  List<String > getMediaPostsByIsbn(String isbn)
      throws NoSuchBookFoundException, NoMediaPostMatchWithIsbnException,
      UnableToProcessQueryException;
}
