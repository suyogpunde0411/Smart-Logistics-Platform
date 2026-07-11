import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';

/**
 * Reusable hook for setting up forms with Zod validation.
 * @param {import('zod').ZodType} schema - The Zod validation schema
 * @param {Object} defaultValues - The default values for the form
 * @returns {import('react-hook-form').UseFormReturn}
 */
export const useZodForm = (schema, defaultValues) => {
  return useForm({
    resolver: zodResolver(schema),
    defaultValues,
  });
};
