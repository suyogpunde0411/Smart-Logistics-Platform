import { useMutation } from '@tanstack/react-query';
import { analyticsService } from '../services/analyticsService';
import * as XLSX from 'xlsx';
import { jsPDF } from 'jspdf';

const download = (blob, filename) => {
  const url = URL.createObjectURL(blob);
  const anchor = document.createElement('a');
  anchor.href = url;
  anchor.download = filename;
  anchor.click();
  URL.revokeObjectURL(url);
};

const toCsv = (rows) => {
  const headers = [...new Set(rows.flatMap((row) => Object.keys(row)))];
  const escape = (value) => `"${String(value ?? '').replaceAll('"', '""')}"`;
  return [headers.join(','), ...rows.map((row) => headers.map((key) => escape(row[key])).join(','))].join('\n');
};

export const useExport = () => {
  const backendExport = useMutation({ mutationFn: analyticsService.exportReport });

  const exportRowsAsCsv = (rows, filename = 'analytics-report.csv') => {
    download(new Blob([toCsv(rows)], { type: 'text/csv;charset=utf-8' }), filename);
  };

  const exportRowsAsExcel = (rows, filename = 'analytics-report.xlsx') => {
    const worksheet = XLSX.utils.json_to_sheet(rows);
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, 'Report Data');
    XLSX.writeFile(workbook, filename);
  };

  const exportRowsAsPdf = (rows, filename = 'analytics-report.pdf', titleText = 'Analytics Report') => {
    const doc = new jsPDF();
    doc.setFontSize(18);
    doc.text(titleText, 14, 22);
    doc.setFontSize(10);
    doc.setTextColor(100);
    doc.text(`Generated on: ${new Date().toLocaleString()}`, 14, 30);
    
    let y = 40;
    if (rows.length > 0) {
      const headers = Object.keys(rows[0]).slice(0, 5); // display first 5 columns to fit page
      
      // Draw Headers
      doc.setFont(undefined, 'bold');
      headers.forEach((header, index) => {
        doc.text(header.replace(/([A-Z])/g, ' $1').toUpperCase(), 14 + (index * 36), y);
      });
      y += 2;
      doc.line(14, y, 196, y);
      y += 6;
      
      // Draw Rows
      doc.setFont(undefined, 'normal');
      rows.slice(0, 25).forEach((row) => {
        headers.forEach((header, index) => {
          const val = String(row[header] ?? '');
          doc.text(val.length > 20 ? val.substring(0, 18) + '..' : val, 14 + (index * 36), y);
        });
        y += 7;
      });
    } else {
      doc.text('No records to export.', 14, y);
    }
    
    doc.save(filename);
  };

  return { ...backendExport, exportRowsAsCsv, exportRowsAsExcel, exportRowsAsPdf };
};

