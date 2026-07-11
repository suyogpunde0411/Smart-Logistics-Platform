import React from 'react';

export const MatchScoreIndicator = ({ score, size = 'md' }) => {
  const percentage = Math.max(0, Math.min(100, score));
  
  // Determine color based on score
  let colorClass = 'text-green-500';
  let bgColorClass = 'text-green-500/20';
  if (percentage < 50) {
    colorClass = 'text-destructive';
    bgColorClass = 'text-destructive/20';
  } else if (percentage < 80) {
    colorClass = 'text-yellow-500';
    bgColorClass = 'text-yellow-500/20';
  }

  const sizeMap = {
    sm: { radius: 14, stroke: 3, text: 'text-[10px]' },
    md: { radius: 20, stroke: 4, text: 'text-xs' },
    lg: { radius: 32, stroke: 6, text: 'text-lg' }
  };

  const { radius, stroke, text } = sizeMap[size];
  const normalizedRadius = radius - stroke * 2;
  const circumference = normalizedRadius * 2 * Math.PI;
  const strokeDashoffset = circumference - (percentage / 100) * circumference;

  return (
    <div className="relative flex items-center justify-center" style={{ width: radius * 2, height: radius * 2 }}>
      <svg height={radius * 2} width={radius * 2} className="transform -rotate-90">
        <circle
          stroke="currentColor"
          fill="transparent"
          strokeWidth={stroke}
          r={normalizedRadius}
          cx={radius}
          cy={radius}
          className={bgColorClass}
        />
        <circle
          stroke="currentColor"
          fill="transparent"
          strokeWidth={stroke}
          strokeDasharray={circumference + ' ' + circumference}
          style={{ strokeDashoffset, transition: 'stroke-dashoffset 0.5s ease 0s' }}
          strokeLinecap="round"
          r={normalizedRadius}
          cx={radius}
          cy={radius}
          className={colorClass}
        />
      </svg>
      <span className={`absolute font-bold ${text} ${colorClass}`}>
        {percentage}%
      </span>
    </div>
  );
};
