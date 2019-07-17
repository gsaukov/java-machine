const margin = { top: 50, right: 70, bottom: 30, left: 50 };

const $chart = d3.select('#vis');
const $svg = $chart.append('svg');
const $plot = $svg.append('g')
    .attr('transform', `translate(${margin.left}, ${margin.top})`);

const parseDate = d3.timeParse('%Y');

// set up scales
const x = d3.scaleTime();
const y = d3.scaleLinear();

const colour = d3.scaleOrdinal(d3.schemeCategory10);

const xAxis = d3.axisBottom()
    .scale(x)
    .ticks(10);

const yAxis = d3.axisLeft()
    .scale(y)
    .ticks(10);

const line = d3.line()
    .x(d => x(d.date))
    .y(d => y(d.value))
    .curve(d3.curveMonotoneX);

function render() {

    const width = parseInt($chart.node().offsetWidth) - margin.left - margin.right;
    const height = parseInt(width * 0.4) - margin.top - margin.bottom;

    $svg.attr('width', width + margin.left + margin.right)
        .attr('height', height + margin.top + margin.bottom);

    x.range([0, width]);
    y.range([height, 0]);

    $plot.select('.axis.x')
        .attr('transform', `translate(0, ${height})`)
        .call(xAxis)
        .select('.domain').remove();

    $plot.select('.axis.y')
        .call(yAxis)
        .call(g => g.select('.tick:last-of-type text').clone()
            .attr('x', 3)
            .attr('text-anchor', 'start')
            .attr('font-weight', 600)
            .text('$ billion'))
        .select('.domain').remove();

    $plot.select('.baseline')
        .attr('x1', 0)
        .attr('x2', width)
        .attr('y1', y(0))
        .attr('y2', y(0))
        .attr('fill', 'none')
        .attr('stroke', '#000')
        .attr('stroke-width', '1px')
        .attr('shape-rendering', 'crispEdges')
        .attr('stroke-dasharray', '3, 3')

    const path = $plot.selectAll('path')
        .attr('d', d => line(d.values))
        .attr('stroke', d => colour(d.name))
        .attr('opacity', d => d.name == 'Highlight' ? 1 : 0.5)
        .attr('id', (d, i) => `line-${d.name}`)

    path.each((d, i) => {
        const sel = d3.select(`#line-${d.name}`);
        const length = sel.node().getTotalLength();

        sel.attr('stroke-dasharray', `${length} ${length}`)
            .attr('stroke-dashoffset', length)
            .transition()
            .duration(5000)
            .attr('stroke-dashoffset', 0)
    })

    $plot.selectAll('.line-label')
        .attr('transform', d => {
            return `translate(${x(d.value.date)}, ${y(d.value.value)})`;
        })
        .attr('x', 5)
        .attr('dy', '.35em')
        .attr('fill', d => colour(d.name))
        .attr('font-weight', d => d.name == 'Highlight' ? 700 : 400)
        .text(d => d.name)
        .attr('opacity', 0)
        .transition()
        .delay(4000)
        .duration(200)
        .attr('opacity', 1);


    <!--glow!!!!!!!!!! section!!!!-->
    var defs = $svg.append("defs");

    var filter = defs.append("filter")
        .attr("id","glow");

    filter.append("feGaussianBlur")
        .attr("class", "blur")
        .attr("stdDeviation","15")
        .attr("result","coloredBlur");

    var feMerge = filter.append("feMerge");
    feMerge.append("feMergeNode")
        .attr("in","coloredBlur");
    feMerge.append("feMergeNode")
        .attr("in","SourceGraphic");
    <!--glow!!!!!!!!!! section!!!!-->
}

function bindData(rawdata) {
    // column headings, for each line
    const keys = rawdata.columns.filter(key => key != 'year');

    rawdata.forEach(d => {
        d.year = parseDate(d.year);
    })

    const data = keys.map(name => {
        return {
            name,
            values: rawdata.map(d => {
                return {date: d.year, value: +d[name]};
            })
        }
    });

    colour.domain(keys);

    // screen dimensions
    x.domain(d3.extent(rawdata, d => d.year));
    y.domain([
        d3.min(data, c => d3.min(c.values, v => v.value)) / 1.05,
        d3.max(data, c => d3.max(c.values, v => v.value)) * 1.1
    ]).nice();

    // bind data to DOM elements
    const $lines = $plot.append('g')
        .attr('class', 'lines glowed')
        .selectAll('.line')
        .data(data)
        .enter()
        .append('g')
        .attr('class', 'line')

    $lines.append('path')
        .attr('class', 'path glowed')

    d3.selectAll(".glowed").style("filter","url(#glow)"); <!--glow!!!!!!!!!! section!!!!-->

    $lines.append('text')
        .datum(d => {
            return {
                name: d.name,
                value: d.values[d.values.length - 1]
            }
        })
        .attr('class', 'line-label')
        .attr('opacity', 0)

    $plot.append('g')
        .attr('class', 'axis x');

    $plot.append('g')
        .attr('class', 'axis y');

    $plot.append('line')
        .attr('class', 'baseline')

    window.addEventListener('resize', debounce(render, 200));
    render();
}

function initChart(url) {
    d3.csv(url).then(bindData);
}

function debounce(func, wait, immediate){
    var timeout, args, context, timestamp, result;
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

    var debounced = function(){
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
};