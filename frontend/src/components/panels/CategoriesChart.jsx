import React from "react";
import { PieChart, Pie } from "recharts";
import { useMemo } from "react";

export default ({ transactions }) => {
  const [inOut, categories] = useMemo(() => {
    const catIn = {};
    const catOut = {};
    transactions.forEach((e) => {
      const name = e.category.name;
      if (e.amount > 0) {
        catIn[name] = (catIn[name] || 0) + e.amount;
      } else if (e.amount < 0) {
        catOut[name] = (catOut[name] || 0) - e.amount;
      }
    });
    const inTotal = Object.values(catIn).reduce((p, c) => c + p || 0, 0);
    const outTotal = Object.values(catOut).reduce((p, c) => c + p || 0, 0);

    const inOut = [
      { name: "in", value: inTotal },
      { name: "out", value: outTotal },
    ];
    const categories = [
      ...Object.entries(catIn).map((e) => ({ name: e[0], value: e[1] }), []),
      ...Object.entries(catOut).map((e) => ({ name: e[0], value: e[1] }), []),
    ];

    return [inOut, categories];
  }, [transactions]);

  return (
    <PieChart width={300} height={300}>
      <Pie
        data={inOut}
        dataKey="value"
        cx={150}
        cy={150}
        outerRadius={60}
        fill="#8884d8"
        label={false}
      />
      <Pie
        data={categories}
        dataKey="value"
        cx={150}
        cy={150}
        innerRadius={70}
        outerRadius={90}
        fill="#82ca9d"
        label={(value) => value.name}
      />
    </PieChart>
  );
};
