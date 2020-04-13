// ========================= GRAPH CHART SECTION ==================================

let AccPerfChart = {
    render: function (chartObj) {
        let margin = chartObj.margin;

        const width = parseInt(chartObj.chart.node().offsetWidth) - margin.left - margin.right;
        const height = parseInt(width * 0.4) - margin.top - margin.bottom;

        chartObj.svg.attr('width', width + margin.left + margin.right)
                .attr('height', height + margin.top + margin.bottom);

        chartObj.x.range([0, width]);
        chartObj.y.range([height, 0]);

        chartObj.plot.select('.axis.x')
            .attr('transform', `translate(0, ${height})`)
            .call(chartObj.xAxis)
            .select('.domain').remove();

        chartObj.plot.select('.axis.y')
            .call(chartObj.yAxis)
            .call(g => g.select('.tick:last-of-type text').clone()
                .attr('x', 0)
                .attr('text-anchor', 'start')
                .attr('font-weight', 600)
                .text('$ tsd'))
            .select('.domain').remove();

        chartObj.plot.select('.baseline')
            .attr('x1', 0)
            .attr('x2', width)
            .attr('y1', chartObj.y(0))
            .attr('y2', chartObj.y(0))
            .attr('fill', 'none')
            .attr('stroke', '#a1afc3')
            .attr('stroke-width', '1px')
            .attr('shape-rendering', 'crispEdges')
            .attr('stroke-dasharray', '3, 3')

        const path = chartObj.plot.selectAll('path')
            .attr('d', d => chartObj.line(d.values))
            .attr('stroke', d => chartObj.colour(d.name))
            .attr('opacity', d => d.name == 'Highlight' ? 1 : 0.5)
            .attr('id', (d, i) => `line-${d.name}`)

        path.each((d, i) => {
            const sel = d3.select(`#line-${d.name}`);
            const length = sel.node().getTotalLength();
            //https://stackoverflow.com/questions/13893127/how-to-draw-a-path-smoothly-from-start-point-to-end-point-in-d3-js
            //if it is not smooth disable glow filter.
            sel.attr('stroke-dasharray', `${length} ${length}`)
                .attr('stroke-dashoffset', length)
                .transition()
                .duration(5000)
                .attr('stroke-dashoffset', 0)
        })

        chartObj.plot.selectAll('.line-label')
            .attr('class', 'line-label glowed')
            .attr('x', 10)
            .attr('y', 30)
            .attr('dy', '.35em')
            .attr('fill', d => chartObj.colour(d.name))
            .attr('font-weight', 700)
            .text(d => d.name + ' ' + d.value.value)
            .attr('opacity', 0)
            .transition()
            .delay(4000)
            .duration(200)
            .attr('opacity', 1);

        chartObj.plot.selectAll('.circle')
        // this move the circle to the end of the line.
            .attr('transform', d => {
                return `translate(${chartObj.x(d.value.date)}, ${chartObj.y(d.value.value)})`;
            })
            .attr('class', 'circle glowed')
            .attr('x', 0)
            .attr('opacity', 0)
            .transition()
            .delay(4000)
            .duration(200)
            .attr('opacity', 1);

        //glow filter
        let defs = chartObj.svg.append("defs");

        let filter = defs.append("filter")
            .attr("id","glow");

        filter.append("feGaussianBlur")
            .attr("class", "blur")
            .attr("stdDeviation","15")
            .attr("result","coloredBlur");

        let feMerge = filter.append("feMerge");
        feMerge.append("feMergeNode")
            .attr("in","coloredBlur");
        feMerge.append("feMergeNode")
            .attr("in","SourceGraphic");
    },

    bindData: function (rawdata, chartObj) {
        // column headings, for each line
        const keys = rawdata.columns.filter(key => key != 'year');

        rawdata.forEach(d => {
            d.year = chartObj.parseDate(d.year);
        })

        const data = keys.map(name => {
            return {
                name,
                values: rawdata.map(d => {
                    return {date: d.year, value: +d[name]};
                })
            }
        });

        chartObj.colour.domain(keys);

        // screen dimensions
        chartObj.x.domain(d3.extent(rawdata, d => d.year));
        chartObj.y.domain([
            d3.min(data, c => d3.min(c.values, v => v.value)) / 1.05,
            d3.max(data, c => d3.max(c.values, v => v.value)) * 1.1
        ]).nice();

        // bind data to DOM elements
        const $lines = chartObj.plot.append('g')
            .attr('class', 'lines glowed')
            .selectAll('.line')
            .data(data)
            .enter()
            .append('g')
            .attr('class', 'line')
            .attr('fill', 'none')
            .attr('stroke-width', '2px')


        $lines.append('path')
            .attr('class', 'path glowed')

        //enable glow
        d3.selectAll(".glowed").style("filter","url(#glow)");

        $lines.append('text')
            .datum(d => {
                return {
                    name: d.name,
                    value: d.values[d.values.length - 1]
                }
            })
            .attr('class', 'line-label')
            .attr('opacity', 0)

        $lines.append('circle')
            .datum(d => {
                return {
                    name: d.name,
                    value: d.values[d.values.length - 1]
                }
            })
            .attr('class', 'circle glowed')
            .attr('r', 4)
            .attr('fill', '#000000')
            .style('stroke', d => chartObj.colour(d.name))
            .style('stroke-width', 2)

        chartObj.plot.append('g')
            .attr('class', 'axis x');

        chartObj.plot.append('g')
            .attr('class', 'axis y');

        chartObj.plot.append('line')
            .attr('class', 'baseline')

    //    window.addEventListener('resize', this.debounce(this.render, 200));
        this.render(chartObj);
    },

    initChart: function (chartObj) {
        d3.csv(chartObj.dataUrl).then(function callbackExecutor(rawdata) {AccPerfChart.bindData (rawdata, chartObj);});
    },

    debounce: function (chartObj, func, wait, immediate){
        let timeout, args, context, timestamp, result;
        if (null == wait) wait = 100;

        function later() {
            var last = Date.now() - timestamp;

            if (last < wait && last >= 0) {
                timeout = setTimeout(later, wait - last);
            } else {
                timeout = null;
                if (!immediate) {
                    result = func.apply(context, args);
                    context = args = null;
                }
            }
        };

        let debounced = function(){
            context = this;
            args = arguments;
            timestamp = Date.now();
            var callNow = immediate && !timeout;
            if (!timeout) timeout = setTimeout(later, wait);
            if (callNow) {
                result = func.apply(context, args);
                context = args = null;
            }

            return result;
        };

        debounced.clear = function() {
            if (timeout) {
                clearTimeout(timeout);
                timeout = null;
            }
        };

        debounced.flush = function() {
            if (timeout) {
                result = func.apply(context, args);
                context = args = null;

                clearTimeout(timeout);
                timeout = null;
            }
        };

        return debounced;
    }
}

// ========================= PIE CHART SECTION ==================================

let PieChart = {

    colors : ["#D2B7ED", "#e4a0d7", "#DB84CB", "#AE88D6", "#ff4cb4", "#6178e5", "#ae73d5", "#ffc800", "#ff953d",
        "#C19FE3", "#D5D2F6", "#f2e700", "#ff5095", "#d285ce", "#a4a8f0", "#bb77d3", "#f5d6ee", "#7a6cdd", "#c77bd0",
        "#ffb80d", "#ff6573", "#eeedfc", "#ed50c2", "#F0E4FB", "#d555ce", "#b388d5", "#ffffff", "#9a61dd", "#c386d3",
        "#b7b9f3", "#FCE3F7", "#dcdbf9", "#b95bd7", "#ff8450", "#4169e1", "#c386d3", "#7466e1", "#F7CCEF", "#7988e9",
        "#ffd800", "#d27fcd", "#626adf", "#f1c9e8", "#ddf60d", "#8f98ec", "#C0BCEE", "#ff7461", "#cacaf6", "#8e6edb",
        "#ABA5E5", "#9f70d8"],

    // var labels = ["Lorem ipsum", "dolor sit", "amet", "consectetur", "adipisicing", "elit", "sed", "do", "eiusmod", "tempor"];


    // [
    //     {"label":"Loremipsum","value":12},
    //     {"label":"dolorsit","value":8},
    //     {"label":"amet","value":6},
    //     {"label":"consectetur","value":11},
    //     {"label":"adipisicing","value":22},
    //     {"label":"elit","value":33},
    //     {"label":"sed","value":17},
    //     {"label":"do","value":44},
    //     {"label":"eiusmod","value":15},
    //     {"label":"tempor","value":27}
    //     ]

    prepareData : function (jsonData, source) {
        let rawData = JSON.parse(jsonData);
        let length = this.colors.length;
        let randomStart = Math.floor(Math.random() * length);
        return rawData.map(function (entry, index) {
            return {label: entry.label, value: entry[source], color: PieChart.colors[(index + randomStart) % length]}
        });
    },

    fill: function (data) {

        /* ------- PIE SLICES -------*/
        let slice = globalBodyContainer.svg.select(".slices").selectAll("path.slice")
            .data(globalBodyContainer.pie(data), globalBodyContainer.key);

        slice.enter()
            .insert("path")
            .attr("class", "slice")
            .style("fill", function(d) { return d.data.color; })
            .merge(slice)
            .transition().duration(1000)
            .attrTween("d", function (d) {
                this._current = this._current || d;
                var interpolate = d3.interpolate(this._current, d);
                this._current = interpolate(0);
                return function (t) {
                    return globalBodyContainer.arc(interpolate(t));
                };
            });

        slice.exit()
            .remove();

        /* ------- TEXT LABELS -------*/

        var text = globalBodyContainer.svg.select(".labels").selectAll("text")
            .data(globalBodyContainer.pie(data), globalBodyContainer.key);

        function midAngle(d) {
            return d.startAngle + (d.endAngle - d.startAngle) / 2;
        }

        text.enter()
            .append("text")
            .attr("dy", ".35em")
            .style("stroke", "white")
            .style("fill", "white")
            .text(function (d) {
                return d.data.label;
            })
            .merge(text)
            .transition().duration(1000)
            .attrTween("transform", function (d) {
                this._current = this._current || d;
                var interpolate = d3.interpolate(this._current, d);
                this._current = interpolate(0);
                return function (t) {
                    var d2 = interpolate(t);
                    var pos = globalBodyContainer.outerArc.centroid(d2);
                    pos[0] = globalBodyContainer.radius * (midAngle(d2) < Math.PI ? 1 : -1);
                    return "translate(" + pos + ")";
                };
            })
            .styleTween("text-anchor", function (d) {
                this._current = this._current || d;
                var interpolate = d3.interpolate(this._current, d);
                this._current = interpolate(0);
                return function (t) {
                    var d2 = interpolate(t);
                    return midAngle(d2) < Math.PI ? "start" : "end";
                };
            });
        text.exit()
            .remove();

        /* ------- SLICE TO TEXT POLYLINES -------*/

        var polyline = globalBodyContainer.svg.select(".lines").selectAll("polyline")
            .data(globalBodyContainer.pie(data), globalBodyContainer.key);

        polyline.enter()
            .append("polyline")
            .merge(polyline)
            .transition().duration(1000)
            .attrTween("points", function (d) {
                this._current = this._current || d;
                var interpolate = d3.interpolate(this._current, d);
                this._current = interpolate(0);
                return function (t) {
                    var d2 = interpolate(t);
                    var pos = globalBodyContainer.outerArc.centroid(d2);
                    pos[0] = globalBodyContainer.radius * 0.95 * (midAngle(d2) < Math.PI ? 1 : -1);
                    return [globalBodyContainer.arc.centroid(d2), globalBodyContainer.outerArc.centroid(d2), pos];
                };
            });

        polyline.exit()
            .remove();
    }
}

// ========================= BAR CHART SECTION ==================================

function BarChart(resp, domainName, size) {
    const parseDate = d3.timeParse("%d-%b-%y");

    let data = resp.map(function (row) {
        return {
            symbol: row.symbol,
            route: row.route,
            volume: Number(row.volume),
            value: Number(row.value),
            date: parseDate(row.date)
        }
    });

    const volumeSeries = fc.autoBandwidth(fc.seriesSvgBar())
        .mainValue(d => d.value)
        .crossValue(d => d.date)
        .decorate(sel =>
            sel.enter()
                .attr('fill', d => d.route === "SELL" ? 'red' : 'green')
                .attr('onmouseover', function(d, i, n) {
                    return 'updateBarChartLegend(' + JSON.stringify(d) + ')';
                })
        );

    // adapt the d3 time scale to add discontinuities, so that weekends are removed
    const xScale = fc.scaleDiscontinuous(d3.scaleTime())
        .discontinuityProvider(fc.discontinuitySkipWeekends());

    const yScale = d3.scaleLinear();

    const chart = fc.chartCartesian(
        xScale,
        yScale
    )
        .yOrient('left')
        .yLabel(domainName + ' ' + size + ' performance $')
        .yTickFormat(d3.format(',.3s'))
        .svgPlotArea(volumeSeries);

    // use the extent component to determine the x and y domain
    const durationDay = 864e5;
    const xExtent = fc.extentDate()
        .accessors([d => d.date])
        // pad by one day on either side of the scale
        .padUnit('domain')
        .pad([durationDay, durationDay]);

    const yExtent = fc.extentLinear()
        .accessors([d => d.value])
        .include([0])
        // pad by 10% at the upper end
        .pad([0, 0.1]);

    // set the domain based on the data
    chart.xDomain(xExtent(data))
        .yDomain(yExtent(data))

    chart.decorate(sel => {
        sel.enter()
           .append("svg")
           .attr('id', 'domainBarChartLegend')
           .style("grid-column", 3)
           .style("grid-row", 3)
           .style("position", "relative")
           .classed("legend", true);
    });

    // select and render
    this.render = function render(elementId) {
        d3.select('#' + elementId)
            .datum(data)
            .call(chart);
    }
}

function updateBarChartLegend(updateData){
    tryPreventDefault();
    
    const dateFormat = function (dateStr) {
        let date = new Date(dateStr);
        return date.getDate() + '-' + date.getMonth() + '-' + date.getFullYear()
    }

    const priceFormat = d3.format(",.2f");

    updateData.value = priceFormat(updateData.value);
    updateData.date = dateFormat(updateData.date);

    removeAllChildren('domainBarChartLegend');
    let el = buildLegendHtml(updateData);
    document.getElementById('domainBarChartLegend').appendChild(el);

    function buildLegendHtml(data) {
        var xmlns = "http://www.w3.org/2000/svg";
        let gElement = document.createElementNS(xmlns, 'g')
        let keys = Object.keys(data);
        let values = Object.values(data);

        for (let i=0; i < keys.length; i++) {
            let key = keys[i];
            key = key.charAt(0).toUpperCase() + key.slice(1)
            let el = buildGridElement('legend-label', "translate(50, " + (i + 1) * 15 + ")", key)
            gElement.appendChild(el);
        }

        for (let i=0; i < values.length; i++) {
            let value = values[i];
            let el = buildGridElement('legend-value', "translate(120, " + (i + 1) * 15 + ")", value)
            gElement.appendChild(el);
        }

        function buildGridElement (clazz, transform, value){
            let textLabel = document.createElementNS(xmlns, 'text');
            textLabel.classList.add(clazz);
            textLabel.setAttribute("fill", "white");
            textLabel.setAttribute("transform", transform);
            textLabel.innerHTML = value;
            return textLabel;
        }

        return gElement;
    }

}
