import EStyleSheet from 'react-native-extended-stylesheet';


export default styles = EStyleSheet.create({
    container: {
        flex: 1,
        zIndex: 900,
        backgroundColor: '#000',
        opacity: 0.5,
        position: 'absolute',
        top: 0,
        left: 0,
        right: 0,
        bottom: 0,
    },
    box: {
        zIndex: 1000,
        borderRadius: 5,
        width: '90%',
        padding: 10,
        height: 'auto',
        backgroundColor: '#fff'
    }

});