package org.sonar.plugins.multibuildstability;

import org.sonar.api.ServerExtension;
import org.sonar.api.ExtensionProvider;

import java.util.List;
import java.util.ArrayList;

public class BuildStabilityExtensionProvider extends ExtensionProvider implements ServerExtension {
    public List provide() {
        List result = new ArrayList();
        for(int i = 0; i<10; i++) {
            result.add(new BuildStabilityWidget(i));
        }
        return result;
    }    
}
