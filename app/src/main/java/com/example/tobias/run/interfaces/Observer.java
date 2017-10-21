package com.example.tobias.run.interfaces;

import java.util.List;

/**
 * Created by Tobi on 10/20/2017.
 */

public interface Observer<T> {
    void updateData(List<T> data);
}
