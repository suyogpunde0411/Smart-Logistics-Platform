import React, { useState, useRef, useEffect } from 'react';
import { Download, ChevronDown, FileText, FileSpreadsheet, File } from 'lucide-react';

export const ExportButton = ({ onExportCsv, onExportExcel, onExportPdf, disabled, label = 'Export' }) => {
  const [isOpen, setIsOpen] = useState(false);
  const dropdownRef = useRef(null);

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setIsOpen(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  return (
    <div className="relative inline-block text-left" ref={dropdownRef}>
      <div className="inline-flex rounded-md shadow-sm">
        <button
          type="button"
          disabled={disabled}
          onClick={() => setIsOpen(!isOpen)}
          className="inline-flex items-center gap-2 rounded-l-md border border-border bg-card px-4 py-2 text-sm font-medium hover:bg-muted focus:outline-none disabled:cursor-not-allowed disabled:opacity-50"
        >
          <Download size={16} />
          {label}
        </button>
        <button
          type="button"
          disabled={disabled}
          onClick={() => setIsOpen(!isOpen)}
          className="inline-flex items-center border-t border-b border-r border-border bg-card px-2 py-2 rounded-r-md hover:bg-muted focus:outline-none disabled:cursor-not-allowed disabled:opacity-50"
          aria-label="Select export format"
        >
          <ChevronDown size={16} />
        </button>
      </div>

      {isOpen && (
        <div className="absolute right-0 mt-2 w-44 origin-top-right rounded-md bg-card border border-border shadow-lg ring-1 ring-black/5 focus:outline-none z-50 animate-in fade-in slide-in-from-top-1 duration-200">
          <div className="py-1">
            {onExportCsv && (
              <button
                type="button"
                onClick={() => {
                  onExportCsv();
                  setIsOpen(false);
                }}
                className="flex w-full items-center gap-2 px-4 py-2 text-sm text-foreground hover:bg-muted"
              >
                <File size={16} className="text-blue-500" />
                Export CSV
              </button>
            )}
            {onExportExcel && (
              <button
                type="button"
                onClick={() => {
                  onExportExcel();
                  setIsOpen(false);
                }}
                className="flex w-full items-center gap-2 px-4 py-2 text-sm text-foreground hover:bg-muted"
              >
                <FileSpreadsheet size={16} className="text-green-500" />
                Export Excel (.xlsx)
              </button>
            )}
            {onExportPdf && (
              <button
                type="button"
                onClick={() => {
                  onExportPdf();
                  setIsOpen(false);
                }}
                className="flex w-full items-center gap-2 px-4 py-2 text-sm text-foreground hover:bg-muted"
              >
                <FileText size={16} className="text-red-500" />
                Export PDF (.pdf)
              </button>
            )}
          </div>
        </div>
      )}
    </div>
  );
};
