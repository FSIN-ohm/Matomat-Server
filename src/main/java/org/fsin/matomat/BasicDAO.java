package org.fsin.matomat;

import java.util.List;

public interface BasicDAO<T> {
    List<T> getAll();
}
