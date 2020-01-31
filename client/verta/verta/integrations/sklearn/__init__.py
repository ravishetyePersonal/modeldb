# -*- coding: utf-8 -*-

"""
scikit-learn dynamic patch that automates logging to Verta during training.

This patch adds a ``run`` parameter to the ``fit()`` methods of most scikit-learn models, and logs
the model's hyperparameters.

.. versionadded:: 0.13.20

Examples
--------
>>> import verta.integrations.sklearn
>>> run = client.set_experiment_run()
>>> model = sklearn.linear_model.LogisticRegression()
>>> model.fit(X, y, run=run)

"""

from ...external import six

from sklearn import (
    linear_model,
    tree,
    svm,
    ensemble,
    neural_network,
    multiclass,
    multioutput,
    isotonic,
    kernel_ridge,
)

from ...external import gorilla

from ... import _utils


classes = [
    linear_model.ARDRegression,
    linear_model.BayesianRidge,
    linear_model.ElasticNet, linear_model.ElasticNetCV,
    linear_model.HuberRegressor,
    linear_model.Lars, linear_model.LarsCV,
    linear_model.Lasso, linear_model.LassoCV,
    linear_model.LassoLars, linear_model.LassoLarsCV, linear_model.LassoLarsIC,
    linear_model.LinearRegression,
    linear_model.LogisticRegression, linear_model.LogisticRegressionCV,
    linear_model.MultiTaskLasso, linear_model.MultiTaskLassoCV,
    linear_model.MultiTaskElasticNet, linear_model.MultiTaskElasticNetCV,
    linear_model.OrthogonalMatchingPursuit, linear_model.OrthogonalMatchingPursuitCV,
    linear_model.PassiveAggressiveClassifier, linear_model.PassiveAggressiveRegressor,
    linear_model.Perceptron,
    linear_model.RANSACRegressor,
    linear_model.Ridge, linear_model.RidgeCV,
    linear_model.RidgeClassifier, linear_model.RidgeClassifierCV,
    linear_model.SGDClassifier, linear_model.SGDRegressor,
    linear_model.TheilSenRegressor,
    tree.DecisionTreeClassifier, tree.DecisionTreeRegressor,
    tree.ExtraTreeClassifier, tree.ExtraTreeRegressor,
    svm.LinearSVC, svm.LinearSVR,
    svm.NuSVC, svm.NuSVR,
    svm.OneClassSVM,
    svm.SVC, svm.SVR,
    ensemble.AdaBoostClassifier, ensemble.AdaBoostRegressor,
    ensemble.BaggingClassifier, ensemble.BaggingRegressor,
    ensemble.ExtraTreesClassifier, ensemble.ExtraTreesRegressor,
    ensemble.GradientBoostingClassifier, ensemble.GradientBoostingRegressor,
    ensemble.IsolationForest,
    ensemble.RandomForestClassifier, ensemble.RandomForestRegressor, ensemble.RandomTreesEmbedding,
    neural_network.BernoulliRBM,
    neural_network.MLPClassifier, neural_network.MLPRegressor,
    isotonic.IsotonicRegression,
    kernel_ridge.KernelRidge,
]


settings = gorilla.Settings(allow_hit=True)

def fit_and_log(self, cls, *args, **kwargs):
    run = kwargs.pop('run', None)
    if run is not None:
        params = self.get_params()

        # remove items that can't be logged as Verta hyperparameters
        cleaned_params = dict()
        for key, val in six.viewitems(params):
            try:
                _utils.python_to_val_proto(val)
            except TypeError:
                continue
            else:
                cleaned_params.update({key: val})

        try:
            run.log_hyperparameters(cleaned_params)
        except:
            pass  # don't halt execution

    original_fit = gorilla.get_original_attribute(cls, 'fit')
    return original_fit(self, *args, **kwargs)

@gorilla.patch(linear_model.ARDRegression)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.ARDRegression, *args, **kwargs)
patch = gorilla.Patch(linear_model.ARDRegression, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.BayesianRidge)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.BayesianRidge, *args, **kwargs)
patch = gorilla.Patch(linear_model.BayesianRidge, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.ElasticNet)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.ElasticNet, *args, **kwargs)
patch = gorilla.Patch(linear_model.ElasticNet, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.ElasticNetCV)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.ElasticNetCV, *args, **kwargs)
patch = gorilla.Patch(linear_model.ElasticNetCV, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.HuberRegressor)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.HuberRegressor, *args, **kwargs)
patch = gorilla.Patch(linear_model.HuberRegressor, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.Lars)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.Lars, *args, **kwargs)
patch = gorilla.Patch(linear_model.Lars, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.LarsCV)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.LarsCV, *args, **kwargs)
patch = gorilla.Patch(linear_model.LarsCV, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.Lasso)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.Lasso, *args, **kwargs)
patch = gorilla.Patch(linear_model.Lasso, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.LassoCV)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.LassoCV, *args, **kwargs)
patch = gorilla.Patch(linear_model.LassoCV, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.LassoLars)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.LassoLars, *args, **kwargs)
patch = gorilla.Patch(linear_model.LassoLars, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.LassoLarsCV)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.LassoLarsCV, *args, **kwargs)
patch = gorilla.Patch(linear_model.LassoLarsCV, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.LassoLarsIC)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.LassoLarsIC, *args, **kwargs)
patch = gorilla.Patch(linear_model.LassoLarsIC, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.LinearRegression)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.LinearRegression, *args, **kwargs)
patch = gorilla.Patch(linear_model.LinearRegression, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.LogisticRegression)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.LogisticRegression, *args, **kwargs)
patch = gorilla.Patch(linear_model.LogisticRegression, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.LogisticRegressionCV)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.LogisticRegressionCV, *args, **kwargs)
patch = gorilla.Patch(linear_model.LogisticRegressionCV, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.MultiTaskLasso)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.MultiTaskLasso, *args, **kwargs)
patch = gorilla.Patch(linear_model.MultiTaskLasso, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.MultiTaskElasticNet)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.MultiTaskElasticNet, *args, **kwargs)
patch = gorilla.Patch(linear_model.MultiTaskElasticNet, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.MultiTaskLassoCV)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.MultiTaskLassoCV, *args, **kwargs)
patch = gorilla.Patch(linear_model.MultiTaskLassoCV, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.MultiTaskElasticNetCV)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.MultiTaskElasticNetCV, *args, **kwargs)
patch = gorilla.Patch(linear_model.MultiTaskElasticNetCV, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.OrthogonalMatchingPursuit)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.OrthogonalMatchingPursuit, *args, **kwargs)
patch = gorilla.Patch(linear_model.OrthogonalMatchingPursuit, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.OrthogonalMatchingPursuitCV)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.OrthogonalMatchingPursuitCV, *args, **kwargs)
patch = gorilla.Patch(linear_model.OrthogonalMatchingPursuitCV, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.PassiveAggressiveClassifier)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.PassiveAggressiveClassifier, *args, **kwargs)
patch = gorilla.Patch(linear_model.PassiveAggressiveClassifier, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.PassiveAggressiveRegressor)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.PassiveAggressiveRegressor, *args, **kwargs)
patch = gorilla.Patch(linear_model.PassiveAggressiveRegressor, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.Perceptron)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.Perceptron, *args, **kwargs)
patch = gorilla.Patch(linear_model.Perceptron, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.RANSACRegressor)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.RANSACRegressor, *args, **kwargs)
patch = gorilla.Patch(linear_model.RANSACRegressor, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.Ridge)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.Ridge, *args, **kwargs)
patch = gorilla.Patch(linear_model.Ridge, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.RidgeClassifier)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.RidgeClassifier, *args, **kwargs)
patch = gorilla.Patch(linear_model.RidgeClassifier, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.RidgeClassifierCV)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.RidgeClassifierCV, *args, **kwargs)
patch = gorilla.Patch(linear_model.RidgeClassifierCV, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.RidgeCV)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.RidgeCV, *args, **kwargs)
patch = gorilla.Patch(linear_model.RidgeCV, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.SGDClassifier)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.SGDClassifier, *args, **kwargs)
patch = gorilla.Patch(linear_model.SGDClassifier, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.SGDRegressor)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.SGDRegressor, *args, **kwargs)
patch = gorilla.Patch(linear_model.SGDRegressor, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(linear_model.TheilSenRegressor)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, linear_model.TheilSenRegressor, *args, **kwargs)
patch = gorilla.Patch(linear_model.TheilSenRegressor, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(tree.DecisionTreeClassifier)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, tree.DecisionTreeClassifier, *args, **kwargs)
patch = gorilla.Patch(tree.DecisionTreeClassifier, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(tree.DecisionTreeRegressor)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, tree.DecisionTreeRegressor, *args, **kwargs)
patch = gorilla.Patch(tree.DecisionTreeRegressor, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(tree.ExtraTreeClassifier)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, tree.ExtraTreeClassifier, *args, **kwargs)
patch = gorilla.Patch(tree.ExtraTreeClassifier, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(tree.ExtraTreeRegressor)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, tree.ExtraTreeRegressor, *args, **kwargs)
patch = gorilla.Patch(tree.ExtraTreeRegressor, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(svm.LinearSVC)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, svm.LinearSVC, *args, **kwargs)
patch = gorilla.Patch(svm.LinearSVC, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(svm.LinearSVR)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, svm.LinearSVR, *args, **kwargs)
patch = gorilla.Patch(svm.LinearSVR, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(svm.NuSVC)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, svm.NuSVC, *args, **kwargs)
patch = gorilla.Patch(svm.NuSVC, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(svm.NuSVR)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, svm.NuSVR, *args, **kwargs)
patch = gorilla.Patch(svm.NuSVR, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(svm.OneClassSVM)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, svm.OneClassSVM, *args, **kwargs)
patch = gorilla.Patch(svm.OneClassSVM, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(svm.SVC)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, svm.SVC, *args, **kwargs)
patch = gorilla.Patch(svm.SVC, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(svm.SVR)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, svm.SVR, *args, **kwargs)
patch = gorilla.Patch(svm.SVR, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(ensemble.AdaBoostClassifier)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, ensemble.AdaBoostClassifier, *args, **kwargs)
patch = gorilla.Patch(ensemble.AdaBoostClassifier, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(ensemble.AdaBoostRegressor)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, ensemble.AdaBoostRegressor, *args, **kwargs)
patch = gorilla.Patch(ensemble.AdaBoostRegressor, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(ensemble.BaggingClassifier)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, ensemble.BaggingClassifier, *args, **kwargs)
patch = gorilla.Patch(ensemble.BaggingClassifier, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(ensemble.BaggingRegressor)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, ensemble.BaggingRegressor, *args, **kwargs)
patch = gorilla.Patch(ensemble.BaggingRegressor, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(ensemble.ExtraTreesClassifier)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, ensemble.ExtraTreesClassifier, *args, **kwargs)
patch = gorilla.Patch(ensemble.ExtraTreesClassifier, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(ensemble.ExtraTreesRegressor)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, ensemble.ExtraTreesRegressor, *args, **kwargs)
patch = gorilla.Patch(ensemble.ExtraTreesRegressor, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(ensemble.GradientBoostingClassifier)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, ensemble.GradientBoostingClassifier, *args, **kwargs)
patch = gorilla.Patch(ensemble.GradientBoostingClassifier, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(ensemble.GradientBoostingRegressor)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, ensemble.GradientBoostingRegressor, *args, **kwargs)
patch = gorilla.Patch(ensemble.GradientBoostingRegressor, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(ensemble.IsolationForest)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, ensemble.IsolationForest, *args, **kwargs)
patch = gorilla.Patch(ensemble.IsolationForest, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(ensemble.RandomForestClassifier)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, ensemble.RandomForestClassifier, *args, **kwargs)
patch = gorilla.Patch(ensemble.RandomForestClassifier, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(ensemble.RandomForestRegressor)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, ensemble.RandomForestRegressor, *args, **kwargs)
patch = gorilla.Patch(ensemble.RandomForestRegressor, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(ensemble.RandomTreesEmbedding)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, ensemble.RandomTreesEmbedding, *args, **kwargs)
patch = gorilla.Patch(ensemble.RandomTreesEmbedding, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(neural_network.BernoulliRBM)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, neural_network.BernoulliRBM, *args, **kwargs)
patch = gorilla.Patch(neural_network.BernoulliRBM, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(neural_network.MLPClassifier)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, neural_network.MLPClassifier, *args, **kwargs)
patch = gorilla.Patch(neural_network.MLPClassifier, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(neural_network.MLPRegressor)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, neural_network.MLPRegressor, *args, **kwargs)
patch = gorilla.Patch(neural_network.MLPRegressor, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(multiclass.OneVsRestClassifier)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, multiclass.OneVsRestClassifier, *args, **kwargs)
patch = gorilla.Patch(multiclass.OneVsRestClassifier, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(multiclass.OneVsOneClassifier)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, multiclass.OneVsOneClassifier, *args, **kwargs)
patch = gorilla.Patch(multiclass.OneVsOneClassifier, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(multiclass.OutputCodeClassifier)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, multiclass.OutputCodeClassifier, *args, **kwargs)
patch = gorilla.Patch(multiclass.OutputCodeClassifier, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(multioutput.ClassifierChain)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, multioutput.ClassifierChain, *args, **kwargs)
patch = gorilla.Patch(multioutput.ClassifierChain, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(multioutput.MultiOutputRegressor)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, multioutput.MultiOutputRegressor, *args, **kwargs)
patch = gorilla.Patch(multioutput.MultiOutputRegressor, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(multioutput.MultiOutputClassifier)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, multioutput.MultiOutputClassifier, *args, **kwargs)
patch = gorilla.Patch(multioutput.MultiOutputClassifier, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(multioutput.RegressorChain)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, multioutput.RegressorChain, *args, **kwargs)
patch = gorilla.Patch(multioutput.RegressorChain, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(isotonic.IsotonicRegression)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, isotonic.IsotonicRegression, *args, **kwargs)
patch = gorilla.Patch(isotonic.IsotonicRegression, 'fit', fit, settings=settings)
gorilla.apply(patch)

@gorilla.patch(kernel_ridge.KernelRidge)
def fit(self, *args, **kwargs):  # pylint: disable=function-redefined
    return fit_and_log(self, kernel_ridge.KernelRidge, *args, **kwargs)
patch = gorilla.Patch(kernel_ridge.KernelRidge, 'fit', fit, settings=settings)
gorilla.apply(patch)
