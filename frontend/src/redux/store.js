import { configureStore, combineReducers } from '@reduxjs/toolkit';
import { persistStore, persistReducer } from 'redux-persist';
import { storage } from './storage';

import authReducer from './slices/authSlice';
import themeReducer from './slices/themeSlice';
import userReducer from './slices/userSlice';
import notificationReducer from './slices/notificationSlice';
import settingsReducer from './slices/settingsSlice';
import dashboardReducer from './slices/dashboardSlice';

const rootReducer = combineReducers({
  auth: authReducer,
  theme: themeReducer,
  user: userReducer,
  notification: notificationReducer,
  settings: settingsReducer,
  dashboard: dashboardReducer,
});

const persistConfig = {
  key: 'root',
  storage,
  whitelist: ['auth', 'theme'],
};

const persistedReducer = persistReducer(persistConfig, rootReducer);

export const store = configureStore({
  reducer: persistedReducer,
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: false,
    }),
});

export const persistor = persistStore(store);
