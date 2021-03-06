/**
 *
 * @param {{endpoints: Array}} instance
 * @param {string} endpointId
 * @returns {string|*}
 */
export const getEndpointUrl = (instance, endpointId) => {
    for(let i = 0, len = instance.endpoints.length; i < len; i++) {
        if(instance.endpoints[i].id === endpointId) {
            return instance.endpoints[i].url;
        }
    }

    return '';
};

/**
 *
 * @param {{axios: Object, endpoints: Array}} instance
 * @param endpointId
 * @param [queryParms={}]
 * @returns {Promise<{data: *, errors: [{message: string, code: string}]}>}
 */
export const get = async (instance, endpointId, queryParms = {}) => {

    const getUrl = getEndpointUrl(instance, endpointId);

    const axiosResponse = await instance.axios.get(getUrl, {
        params: queryParms
    });

    const endpointResponse = axiosResponse.data;

    return endpointResponse;
};

/**
 *
 * @param {{axios: Object, endpoints: Array}} instance
 * @param {string} endpointId
 * @param {{Object}} payload
 * @param [queryParams={}]
 * @returns {Promise<{data: *, errors: [{message: string, code: string}]}>}
 */
export const post = async (instance, endpointId, payload = {}, queryParams = {}) => {
    const postUrl = getEndpointUrl(instance, endpointId);

    const optionalConfig = {};

    if(queryParams) {
        optionalConfig.params = queryParams;
    }

    const axiosResponse = await instance.axios.post(postUrl, payload, optionalConfig);

    const endpointResponse = axiosResponse.data;

    return endpointResponse;
};