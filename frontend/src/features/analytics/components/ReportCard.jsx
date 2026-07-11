import React from 'react';

export const ReportCard = ({ title, description, onGenerate, disabled }) => (
  <article className="rounded-xl border border-border bg-card p-4"><h3 className="font-medium">{title}</h3><p className="mt-1 text-sm text-muted-foreground">{description}</p><button type="button" disabled={disabled} onClick={onGenerate} className="mt-4 text-sm font-medium text-primary hover:underline disabled:opacity-50">Generate report</button></article>
);
