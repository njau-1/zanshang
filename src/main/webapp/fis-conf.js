fis.set('project.files', ['/static/**', '/WEB-INF/**']);
fis.set('project.fileType.text', 'ftl');
fis.match('*.js', {
    optimizer: fis.plugin('uglify-js')
});

fis.match('*.css', {
    optimizer: fis.plugin('clean-css')
});

fis.match(/^\/static\/(.+)/, {
    domain: 'http://7xkqu4.com2.z0.glb.qiniucdn.com',
    useHash: true,
    url: '/$1'
});

fis.match('*.ftl', {
    parser: fis.plugin('ftl')
});
