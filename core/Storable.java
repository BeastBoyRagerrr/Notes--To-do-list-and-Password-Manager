package core;

import exceptions.SaveFailedException;
import java.io.IOException;

public interface Storable {
    void saveToFile(String filePath) throws IOException, SaveFailedException;
    void loadFromFile(String filePath) throws IOException, ClassNotFoundException;
}
