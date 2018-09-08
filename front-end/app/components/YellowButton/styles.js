import EStyleSheet from 'react-native-extended-stylesheet';


export default styles = EStyleSheet.create({
    container: {
        width: 50,
        height: 50,
        position: 'absolute',
        bottom: 10,
        right: 10,
        backgroundColor: '#f1a51e',
        borderRadius: 500,
        alignItems: 'center',
        justifyContent: 'center',
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.2,
        shadowRadius: 0,
    }
});