package ai.verta.client.entities

import ai.verta.swagger._public.modeldb.model.ModeldbExperimentRun
import ai.verta.swagger.client.ClientSet

class ExperimentRun(clientSet: ClientSet, val expt: Experiment, val run: ModeldbExperimentRun) {

}
