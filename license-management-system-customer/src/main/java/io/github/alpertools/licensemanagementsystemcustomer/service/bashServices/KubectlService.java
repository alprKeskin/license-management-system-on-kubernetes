package io.github.alpertools.licensemanagementsystemcustomer.service.bashServices;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KubectlService {

    @Autowired
    private BashScriptRunnerService bashScriptRunnerService;

    public String testBash() {
        String bashScriptName = "test.sh";
        return bashScriptRunnerService.runBashScript(bashScriptName);
    }

    public String getPods() {
        String bashScriptName = "get-pods.sh";
        return bashScriptRunnerService.runBashScript(bashScriptName);
    }

}
