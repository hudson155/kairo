import React, { useContext } from 'react';
import LimberApi from '../api/LimberApi';
import env from '../env';

const unauthenticatedApi = new LimberApi(env.LIMBER_API_BASE_URL, () => Promise.resolve(undefined));

const Context = React.createContext<LimberApi>(unauthenticatedApi);

export const useLimberApi = (): LimberApi => useContext(Context);
